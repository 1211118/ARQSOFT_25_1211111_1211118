package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.MongoLending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingConverter;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MongoLendingRepository implements LendingRepository {

    private final MongoTemplate mongoTemplate;
    private final LendingConverter lendingConverter;

    @Autowired
    public MongoLendingRepository(MongoTemplate mongoTemplate, LendingConverter lendingConverter) {
        this.mongoTemplate = mongoTemplate;
        this.lendingConverter = lendingConverter;
    }

    @Override
public Optional<Lending> findByLendingNumber(String lendingNumber) {
    Query query = new Query(Criteria.where("lendingNumber").is(lendingNumber));
    MongoLending mongoLending = mongoTemplate.findOne(query, MongoLending.class);

    // Lida com Optional retornado por toLending
    return lendingConverter.toLending(mongoLending);
}

@Override
public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
    Query query = new Query(Criteria.where("readerDetailsId").is(readerNumber).and("bookId").is(isbn));
    List<MongoLending> mongoLendings = mongoTemplate.find(query, MongoLending.class);

    
    return mongoLendings.stream()
            .map(lendingConverter::toLending)
            .flatMap(Optional::stream) 
            .collect(Collectors.toList());
}

    @Override
    public int getCountFromCurrentYear() {
        Query query = new Query(Criteria.where("startDate").gte(LocalDate.now().withDayOfYear(1)));
        return (int) mongoTemplate.count(query, MongoLending.class);
    }

    @Override
public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
    Query query = new Query(Criteria.where("readerDetailsId").is(readerNumber).and("returnedDate").is(null));
    List<MongoLending> mongoLendings = mongoTemplate.find(query, MongoLending.class);

    
    return mongoLendings.stream()
            .map(lendingConverter::toLending)
            .flatMap(Optional::stream) 
            .collect(Collectors.toList());
}

    @Override
    public Double getAverageDuration() {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("returnedDate").exists(true)),
            Aggregation.project().andExpression("dayOfMonth(returnedDate) - dayOfMonth(startDate)").as("duration"),
            Aggregation.group().avg("duration").as("averageDuration")
        );

        AggregationResults<Double> result = mongoTemplate.aggregate(aggregation, "lendings", Double.class);
        return result.getUniqueMappedResult();
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("bookId").is(isbn).and("returnedDate").exists(true)),
            Aggregation.project().andExpression("dayOfMonth(returnedDate) - dayOfMonth(startDate)").as("duration"),
            Aggregation.group().avg("duration").as("averageDuration")
        );

        AggregationResults<Double> result = mongoTemplate.aggregate(aggregation, "lendings", Double.class);
        return result.getUniqueMappedResult();
    }

    @Override
public List<Lending> getOverdue(Page page) {
    Query query = new Query(Criteria.where("returnedDate").is(null).and("limitDate").lt(LocalDate.now()))
            .skip((page.getNumber() - 1) * page.getLimit())
            .limit(page.getLimit());

    List<MongoLending> mongoLendings = mongoTemplate.find(query, MongoLending.class);

    // Converte Optional<Lending> para Lending, ignorando os Optionals vazios
    return mongoLendings.stream()
            .map(lendingConverter::toLending)
            .flatMap(Optional::stream) // Desempacota os Optionals
            .collect(Collectors.toList());
}

@Override
public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
    Query query = new Query();
    if (readerNumber != null) query.addCriteria(Criteria.where("readerDetailsId").is(readerNumber));
    if (isbn != null) query.addCriteria(Criteria.where("bookId").is(isbn));
    if (returned != null) query.addCriteria(Criteria.where("returnedDate").exists(returned));
    if (startDate != null) query.addCriteria(Criteria.where("startDate").gte(startDate));
    if (endDate != null) query.addCriteria(Criteria.where("startDate").lte(endDate));

    query.skip((page.getNumber() - 1) * page.getLimit()).limit(page.getLimit());

    List<MongoLending> mongoLendings = mongoTemplate.find(query, MongoLending.class);

    // Converte Optional<Lending> para Lending, ignorando os Optionals vazios
    return mongoLendings.stream()
            .map(lendingConverter::toLending)
            .flatMap(Optional::stream) // Desempacota os Optionals
            .collect(Collectors.toList());
}

@Override
public Lending save(Lending lending) {
    MongoLending mongoLending = lendingConverter.toMongoLending(lending);
    mongoTemplate.save(mongoLending);
    return lending;
}
    @Override
    public void delete(Lending lending) {
        MongoLending mongoLending = lendingConverter.toMongoLending(lending);
        mongoTemplate.remove(mongoLending);
    }

    @Override
    public Optional<Genre> findByReaderDetails(ReaderDetails reader) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByReaderDetails'");
    }
}
