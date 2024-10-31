package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.MongoGenre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MongoGenreRepository implements GenreRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoGenreRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Iterable<Genre> findAll() {
        List<MongoGenre> mongoGenres = mongoTemplate.findAll(MongoGenre.class);
        return mongoGenres.stream()
                .map(GenreConverter::toJpaGenre)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre").is(genreName));
        MongoGenre mongoGenre = mongoTemplate.findOne(query, MongoGenre.class);
        return Optional.ofNullable(mongoGenre).map(GenreConverter::toJpaGenre);
    }

    @Override
    public Genre save(Genre genre) {
        MongoGenre mongoGenre = GenreConverter.toMongoGenre(genre);
        MongoGenre savedMongoGenre = mongoTemplate.save(mongoGenre);
        return GenreConverter.toJpaGenre(savedMongoGenre);
    }

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("books", "_id", "genreId", "books"),
                Aggregation.unwind("books"),
                Aggregation.group("genre").count().as("bookCount"),
                Aggregation.sortByCount("bookCount"),
                Aggregation.limit(5)
        );

        AggregationResults<GenreBookCountDTO> results = mongoTemplate.aggregate(
                aggregation, "genres", GenreBookCountDTO.class
        );
        
        List<GenreBookCountDTO> resultList = results.getMappedResults();
        return new PageImpl<>(resultList, pageable, resultList.size());
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        int days = month.lengthOfMonth();
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(days);

        MatchOperation matchOperation = Aggregation.match(Criteria.where("startDate")
                .gte(startOfMonth)
                .lte(endOfMonth));

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("genre")
                        .count().as("loanCount")
                        .avg(Aggregation.ROOT).as("dailyAvgLoans")
        );

        AggregationResults<GenreLendingsDTO> results = mongoTemplate.aggregate(
                aggregation, "lendings", GenreLendingsDTO.class
        );
        return results.getMappedResults();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        LocalDate twelveMonthsAgo = LocalDate.now().minusMonths(12);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("startDate").gte(twelveMonthsAgo)),
                Aggregation.project("genre")
                        .andExpression("year(startDate)").as("year")
                        .andExpression("month(startDate)").as("month"),
                Aggregation.group("genre", "year", "month")
                        .count().as("lendingCount")
        );

        AggregationResults<GenreLendingsPerMonthDTO> results = mongoTemplate.aggregate(
                aggregation, "lendings", GenreLendingsPerMonthDTO.class
        );
        return results.getMappedResults();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("startDate").gte(startDate).lte(endDate).and("returnedDate").exists(true)),
                Aggregation.project("genre")
                        .andExpression("year(startDate)").as("year")
                        .andExpression("month(startDate)").as("month")
                        .andExpression("returnedDate - startDate").as("durationInDays"),
                Aggregation.group("genre", "year", "month")
                        .avg("durationInDays").as("averageDuration")
        );

        AggregationResults<GenreLendingsPerMonthDTO> results = mongoTemplate.aggregate(
                aggregation, "lendings", GenreLendingsPerMonthDTO.class
        );
        return results.getMappedResults();
    }

    @Override
    public void delete(Genre genre) {
        MongoGenre mongoGenre = GenreConverter.toMongoGenre(genre);
        mongoTemplate.remove(mongoGenre);
    }
}
