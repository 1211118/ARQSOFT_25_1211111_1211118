package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import lombok.RequiredArgsConstructor;

@Repository
@CacheConfig(cacheNames = "users")
@RequiredArgsConstructor
public class SpringDataUserRepository implements UserRepository {

    // Dependência para o EntityManager, injetada via construtor
    private final EntityManager em;

    @Override
    @CacheEvict(allEntries = true)
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        for (S entity : entities) {
            savedEntities.add(this.save(entity));
        }
        return savedEntities;
    }

    @Override
    @Caching(evict = { @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
    public <S extends User> S save(S entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        return entity;
    }

    @Override
    @Cacheable
    public Optional<User> findById(Long objectId) {
        return Optional.ofNullable(em.find(User.class, objectId));
    }

    @Override
    @Cacheable
    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.where(cb.equal(root.get("username"), username));

        TypedQuery<User> query = em.createQuery(cq);
        List<User> users = query.getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(query.getUsername())) {
            predicates.add(cb.equal(root.get("username"), query.getUsername()));
        }
        if (StringUtils.hasText(query.getFullName())) {
            predicates.add(cb.like(root.get("fullName"), "%" + query.getFullName() + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.or(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        TypedQuery<User> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult((page.getNumber() - 1) * page.getLimit());
        typedQuery.setMaxResults(page.getLimit());

        return typedQuery.getResultList();
    }

    @Override
    @Cacheable
    public List<User> findByNameName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.where(cb.equal(root.get("name").get("name"), name));

        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Cacheable
    public List<User> findByNameNameContains(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.where(cb.like(root.get("name").get("name"), "%" + name + "%"));

        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(User user) {
        User managedUser = em.contains(user) ? user : em.merge(user);
        em.remove(managedUser);
    }
}