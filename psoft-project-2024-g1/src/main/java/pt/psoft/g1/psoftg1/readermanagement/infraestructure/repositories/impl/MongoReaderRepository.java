package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.readermanagement.model.MongoReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderConverter;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary  // Define este repositório como implementação principal de ReaderRepository
@Repository
@RequiredArgsConstructor
public class MongoReaderRepository implements ReaderRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        Query query = new Query(Criteria.where("readerNumber").is(readerNumber));
        MongoReaderDetails mongoReader = mongoTemplate.findOne(query, MongoReaderDetails.class);
        return Optional.ofNullable(mongoReader != null ? ReaderConverter.toReaderDetails(mongoReader) : null);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        Query query = new Query(Criteria.where("phoneNumber").is(phoneNumber));
        List<MongoReaderDetails> mongoReaders = mongoTemplate.find(query, MongoReaderDetails.class);
        return mongoReaders.stream()
                           .map(ReaderConverter::toReaderDetails)
                           .collect(Collectors.toList());
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        // Implementa a busca pelo nome de usuário caso este campo exista no MongoDB
        Query query = new Query(Criteria.where("username").is(username));
        MongoReaderDetails mongoReader = mongoTemplate.findOne(query, MongoReaderDetails.class);
        return Optional.ofNullable(mongoReader != null ? ReaderConverter.toReaderDetails(mongoReader) : null);
    }

    @Override
    public Optional<ReaderDetails> findByUserId(Long userId) {
        Query query = new Query(Criteria.where("readerId").is(userId.toString()));
        MongoReaderDetails mongoReader = mongoTemplate.findOne(query, MongoReaderDetails.class);
        return Optional.ofNullable(mongoReader != null ? ReaderConverter.toReaderDetails(mongoReader) : null);
    }

    @Override
    public int getCountFromCurrentYear() {
        LocalDate currentYearStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        Query query = new Query(Criteria.where("createdAt").gte(currentYearStart));
        return (int) mongoTemplate.count(query, MongoReaderDetails.class);
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        MongoReaderDetails mongoReader = ReaderConverter.toMongoReaderDetails(readerDetails);
        mongoTemplate.save(mongoReader);
        return readerDetails;
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        List<MongoReaderDetails> mongoReaders = mongoTemplate.findAll(MongoReaderDetails.class);
        return mongoReaders.stream()
                           .map(ReaderConverter::toReaderDetails)
                           .collect(Collectors.toList());
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<MongoReaderDetails> mongoReaders = mongoTemplate.find(query, MongoReaderDetails.class);
        long total = mongoTemplate.count(new Query(), MongoReaderDetails.class);
        List<ReaderDetails> readers = mongoReaders.stream()
                                                  .map(ReaderConverter::toReaderDetails)
                                                  .collect(Collectors.toList());
        return new PageImpl<>(readers, pageable, total);
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        // Este método precisa de uma agregação mais complexa; para simplificação, usaremos uma consulta básica
        Query query = new Query(Criteria.where("interestList.genre").is(genre)
                                        .and("startDate").gte(startDate)
                                        .and("endDate").lte(endDate))
                       .with(pageable);
        
        List<MongoReaderDetails> mongoReaders = mongoTemplate.find(query, MongoReaderDetails.class);
        List<ReaderBookCountDTO> readerBookCounts = mongoReaders.stream()
                                                                .map(mr -> new ReaderBookCountDTO(ReaderConverter.toReaderDetails(mr), 0L)) // Contagem fictícia
                                                                .collect(Collectors.toList());
        long total = mongoTemplate.count(query, MongoReaderDetails.class);
        return new PageImpl<>(readerBookCounts, pageable, total);
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        MongoReaderDetails mongoReader = ReaderConverter.toMongoReaderDetails(readerDetails);
        mongoTemplate.remove(mongoReader);
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        Query mongoQuery = new Query();

        if (query.getName() != null && !query.getName().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("name").regex(".*" + query.getName() + ".*", "i"));
        }
        if (query.getEmail() != null && !query.getEmail().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("username").is(query.getEmail()));
        }
        if (query.getPhoneNumber() != null && !query.getPhoneNumber().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("phoneNumber").is(query.getPhoneNumber()));
        }

        mongoQuery.skip((page.getNumber() - 1) * page.getLimit()).limit(page.getLimit());
        List<MongoReaderDetails> mongoReaders = mongoTemplate.find(mongoQuery, MongoReaderDetails.class);
        
        return mongoReaders.stream()
                           .map(ReaderConverter::toReaderDetails)
                           .collect(Collectors.toList());
    }

}
