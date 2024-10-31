package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.MongoUser;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.UserConverter;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MongoUserRepository implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<MongoUser> mongoUsers = new ArrayList<>();
        entities.forEach(entity -> mongoUsers.add(UserConverter.toMongoUser(entity)));
        mongoTemplate.insertAll(mongoUsers);
        return (List<S>) mongoUsers.stream().map(UserConverter::toUser).collect(Collectors.toList());
    }

    @Override
public <S extends User> S save(S entity) {
    // Garantir que a senha seja armazenada em texto puro
    MongoUser mongoUser = UserConverter.toMongoUser(entity);
    // Remova qualquer chamada ao passwordEncoder
    mongoTemplate.save(mongoUser);
    return (S) UserConverter.toUser(mongoUser);
}

    @Override
    public Optional<User> findById(Long objectId) {
        MongoUser mongoUser = mongoTemplate.findById(objectId.toString(), MongoUser.class);
        return Optional.ofNullable(mongoUser).map(UserConverter::toUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        MongoUser mongoUser = mongoTemplate.findOne(query, MongoUser.class);
        return Optional.ofNullable(mongoUser).map(UserConverter::toUser);
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        Query mongoQuery = new Query();
        if (StringUtils.hasText(query.getUsername())) {
            mongoQuery.addCriteria(Criteria.where("username").is(query.getUsername()));
        }
        if (StringUtils.hasText(query.getFullName())) {
            mongoQuery.addCriteria(Criteria.where("name.name").regex(".*" + query.getFullName() + ".*"));
        }
        mongoQuery.with(Sort.by(Sort.Direction.DESC, "createdAt"))
                  .skip((page.getNumber() - 1) * page.getLimit())
                  .limit(page.getLimit());

        List<MongoUser> mongoUsers = mongoTemplate.find(mongoQuery, MongoUser.class);
        return mongoUsers.stream().map(UserConverter::toUser).collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameName(String name) {
        Query query = new Query(Criteria.where("name.name").is(name));
        List<MongoUser> mongoUsers = mongoTemplate.find(query, MongoUser.class);
        return mongoUsers.stream().map(UserConverter::toUser).collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        Query query = new Query(Criteria.where("name.name").regex(".*" + name + ".*"));
        List<MongoUser> mongoUsers = mongoTemplate.find(query, MongoUser.class);
        return mongoUsers.stream().map(UserConverter::toUser).collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        MongoUser mongoUser = UserConverter.toMongoUser(user);
        mongoTemplate.remove(mongoUser);
    }

    
}
