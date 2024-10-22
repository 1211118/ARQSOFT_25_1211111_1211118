package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class SpringDataReaderRepository implements ReaderRepository {

    private final EntityManager em;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(@NotNull String readerNumber) {
        TypedQuery<ReaderDetails> query = em.createQuery(
                "SELECT r FROM ReaderDetails r WHERE r.readerNumber.readerNumber = :readerNumber", ReaderDetails.class);
        query.setParameter("readerNumber", readerNumber);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(@NotNull String phoneNumber) {
        TypedQuery<ReaderDetails> query = em.createQuery(
                "SELECT r FROM ReaderDetails r WHERE r.phoneNumber.phoneNumber = :phoneNumber", ReaderDetails.class);
        query.setParameter("phoneNumber", phoneNumber);
        return query.getResultList();
    }

    @Override
    public Optional<ReaderDetails> findByUsername(@NotNull String username) {
        TypedQuery<ReaderDetails> query = em.createQuery(
                "SELECT r FROM ReaderDetails r JOIN User u ON r.reader.id = u.id WHERE u.username = :username", ReaderDetails.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    @Override
    public Optional<ReaderDetails> findByUserId(@NotNull Long userId) {
        TypedQuery<ReaderDetails> query = em.createQuery(
                "SELECT r FROM ReaderDetails r JOIN User u ON r.reader.id = u.id WHERE u.id = :userId", ReaderDetails.class);
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst();
    }

    @Override
    public int getCountFromCurrentYear() {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(rd) FROM ReaderDetails rd JOIN User u ON rd.reader.id = u.id WHERE YEAR(u.createdAt) = YEAR(CURRENT_DATE)", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        if (readerDetails.getPk() == null) {
            em.persist(readerDetails);
        } else {
            em.merge(readerDetails);
        }
        return readerDetails;
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return em.createQuery("SELECT r FROM ReaderDetails r", ReaderDetails.class).getResultList();
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        TypedQuery<ReaderDetails> query = em.createQuery(
                "SELECT rd FROM ReaderDetails rd JOIN Lending l ON l.readerDetails.pk = rd.pk GROUP BY rd ORDER BY COUNT(l) DESC", ReaderDetails.class);
        // Implement pageable logic manually, since this is a custom query.
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, countLendings());  // countLendings() is a helper method for total count.
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        TypedQuery<ReaderBookCountDTO> query = em.createQuery(
                "SELECT NEW pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO(rd, COUNT(l)) " +
                        "FROM ReaderDetails rd " +
                        "JOIN Lending l ON l.readerDetails.pk = rd.pk " +
                        "JOIN Book b ON b.pk = l.book.pk " +
                        "JOIN Genre g ON g.pk = b.genre.pk " +
                        "WHERE g.genre = :genre " +
                        "AND l.startDate >= :startDate " +
                        "AND l.startDate <= :endDate " +
                        "GROUP BY rd.pk " +
                        "ORDER BY COUNT(l.pk) DESC", ReaderBookCountDTO.class);
        query.setParameter("genre", genre);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        // Pageable handling logic.
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, countBooksByGenre(genre, startDate, endDate)); // countBooksByGenre is a helper.
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        if (em.contains(readerDetails)) {
            em.remove(readerDetails);
        } else {
            em.remove(em.merge(readerDetails));
        }
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<ReaderDetails> cq = cb.createQuery(ReaderDetails.class);
        final Root<ReaderDetails> readerDetailsRoot = cq.from(ReaderDetails.class);
        Join<ReaderDetails, User> userJoin = readerDetailsRoot.join("reader");

        cq.select(readerDetailsRoot);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getName())) { 
            where.add(cb.like(userJoin.get("name").get("name"), "%" + query.getName() + "%"));
        }
        if (StringUtils.hasText(query.getEmail())) {
            where.add(cb.equal(userJoin.get("username"), query.getEmail()));
        }
        if (StringUtils.hasText(query.getPhoneNumber())) {
            where.add(cb.equal(readerDetailsRoot.get("phoneNumber").get("phoneNumber"), query.getPhoneNumber()));
        }

        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }

        final TypedQuery<ReaderDetails> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * ((pt.psoft.g1.psoftg1.shared.services.Page) page).getLimit());
        q.setMaxResults(((pt.psoft.g1.psoftg1.shared.services.Page) page).getLimit());

        return q.getResultList();
    }

    // Helper methods to support pagination and count logic.
    private long countLendings() {
        return em.createQuery("SELECT COUNT(l) FROM Lending l", Long.class).getSingleResult();
    }

    private long countBooksByGenre(String genre, LocalDate startDate, LocalDate endDate) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(l) " +
                        "FROM Lending l " +
                        "JOIN Book b ON b.pk = l.book.pk " +
                        "JOIN Genre g ON g.pk = b.genre.pk " +
                        "WHERE g.genre = :genre " +
                        "AND l.startDate >= :startDate " +
                        "AND l.startDate <= :endDate", Long.class);
        query.setParameter("genre", genre);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getSingleResult();
    }
}
