package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorConverter;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AuthorMongoRepositoryImpl implements AuthorRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        AuthorMongo authorMongo = mongoTemplate.findById(authorNumber.toString(), AuthorMongo.class);
        return Optional.ofNullable(AuthorConverter.toJpa(authorMongo));
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        // MongoDB query to find authors by name prefix
        Query query = new Query();
    query.addCriteria(Criteria.where("name.name").regex("^" + name, "i"));  // Usando regex para fazer a busca por prefixo
    
    List<AuthorMongo> authorsMongo = mongoTemplate.find(query, AuthorMongo.class);
    return authorsMongo.stream()
            .map(AuthorConverter::toJpa)
            .collect(Collectors.toList());
    }

    private Object where(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'where'");
    }

    @Override
    public List<Author> searchByNameName(String name) {
        // MongoDB query to find authors by exact name
        Query query = new Query();
    query.addCriteria(Criteria.where("name.name").is(name));  // Usando .is() para fazer a busca exata
    
    List<AuthorMongo> authorsMongo = mongoTemplate.find(query, AuthorMongo.class);
    return authorsMongo.stream()
            .map(AuthorConverter::toJpa)
            .collect(Collectors.toList());
    }

    @Override
    public Author save(Author author) {
        AuthorMongo authorMongo = AuthorConverter.toMongo(author);
        mongoTemplate.save(authorMongo);
        return AuthorConverter.toJpa(authorMongo);
    }

    @Override
    public Iterable<Author> findAll() {
        List<AuthorMongo> authorsMongo = mongoTemplate.findAll(AuthorMongo.class);
        return authorsMongo.stream()
                .map(AuthorConverter::toJpa)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
        // Implement MongoDB-specific logic for lendings (if required)
        return Page.empty();  // Placeholder implementation
    }

    @Override
    public void delete(Author author) {
        AuthorMongo authorMongo = AuthorConverter.toMongo(author);
        mongoTemplate.remove(authorMongo);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        // Implement MongoDB-specific logic for co-authors
        return List.of();  // Placeholder implementation
    }
}
