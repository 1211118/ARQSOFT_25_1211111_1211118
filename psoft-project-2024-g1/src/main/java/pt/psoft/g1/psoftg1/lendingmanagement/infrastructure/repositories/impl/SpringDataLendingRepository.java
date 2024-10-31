package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataLendingRepository implements LendingRepository {

    private final EntityManager em;

    @Override
    @Query("SELECT l " +
            "FROM Lending l " +
            "WHERE l.lendingNumber.lendingNumber = :lendingNumber")
    public Optional<Lending> findByLendingNumber(@Param("lendingNumber") String lendingNumber) {
        return em.createQuery(
                "SELECT l FROM Lending l WHERE l.lendingNumber.lendingNumber = :lendingNumber", Lending.class)
                .setParameter("lendingNumber", lendingNumber)
                .getResultStream().findFirst();
    }

    @Override
    @Query("SELECT l " +
            "FROM Lending l " +
            "JOIN Book b ON l.book.pk = b.pk " +
            "JOIN ReaderDetails r ON l.readerDetails.pk = r.pk " +
            "WHERE b.isbn.isbn = :isbn " +
            "AND r.readerNumber.readerNumber = :readerNumber ")
    public List<Lending> listByReaderNumberAndIsbn(@Param("readerNumber") String readerNumber, @Param("isbn") String isbn) {
        return em.createQuery(
                "SELECT l FROM Lending l " +
                        "JOIN l.book b " +
                        "JOIN l.readerDetails r " +
                        "WHERE b.isbn.isbn = :isbn " +
                        "AND r.readerNumber.readerNumber = :readerNumber", Lending.class)
                .setParameter("isbn", isbn)
                .setParameter("readerNumber", readerNumber)
                .getResultList();
    }

    @Override
    @Query("SELECT COUNT (l) " +
            "FROM Lending l " +
            "WHERE YEAR(l.startDate) = YEAR(CURRENT_DATE)")
    public int getCountFromCurrentYear() {
        return em.createQuery(
                "SELECT COUNT(l) FROM Lending l WHERE YEAR(l.startDate) = YEAR(CURRENT_DATE)", Long.class)
                .getSingleResult().intValue();
    }

    @Override
    @Query("SELECT l " +
            "FROM Lending l " +
            "JOIN ReaderDetails r ON l.readerDetails.pk = r.pk " +
            "WHERE r.readerNumber.readerNumber = :readerNumber " +
            "AND l.returnedDate IS NULL")
    public List<Lending> listOutstandingByReaderNumber(@Param("readerNumber") String readerNumber) {
        return em.createQuery(
                "SELECT l FROM Lending l " +
                        "JOIN l.readerDetails r " +
                        "WHERE r.readerNumber.readerNumber = :readerNumber " +
                        "AND l.returnedDate IS NULL", Lending.class)
                .setParameter("readerNumber", readerNumber)
                .getResultList();
    }

    @Override
    @Query(value =
            "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) " +
            "FROM Lending l", nativeQuery = true)
    public Double getAverageDuration() {
        return (Double) em.createNativeQuery(
                "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) FROM Lending l")
                .getResultStream()
                .findFirst()
                .map(result -> (Double) result)
                .orElse(null);
    }

    @Override
    @Query(value =
            "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) " +
                    "FROM Lending l " +
                    "JOIN Book b ON l.book.pk = b.pk " +
                    "WHERE b.isbn.isbn = :isbn", nativeQuery = true)
    public Double getAvgLendingDurationByIsbn(@Param("isbn") String isbn) {
        return (Double) em.createNativeQuery(
                "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) " +
                        "FROM Lending l " +
                        "JOIN BOOK b ON l.book_pk = b.pk " +
                        "WHERE b.isbn = :isbn")
                .setParameter("isbn", isbn)
                .getResultStream()
                .findFirst()
                .map(result -> (Double) result)
                .orElse(null);
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        final Root<Lending> root = cq.from(Lending.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();
        where.add(cb.isNull(root.get("returnedDate")));
        where.add(cb.lessThan(root.get("limitDate"), LocalDate.now()));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("limitDate")));

        final TypedQuery<Lending> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        final Root<Lending> lendingRoot = cq.from(Lending.class);
        final Join<Lending, Book> bookJoin = lendingRoot.join("book");
        final Join<Lending, ReaderDetails> readerDetailsJoin = lendingRoot.join("readerDetails");
        cq.select(lendingRoot);

        final List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(readerNumber))
            where.add(cb.like(readerDetailsJoin.get("readerNumber").get("readerNumber"), readerNumber));
        if (StringUtils.hasText(isbn))
            where.add(cb.like(bookJoin.get("isbn").get("isbn"), isbn));
        if (returned != null) {
            if (returned) {
                where.add(cb.isNotNull(lendingRoot.get("returnedDate")));
            } else {
                where.add(cb.isNull(lendingRoot.get("returnedDate")));
            }
        }
        if (startDate != null)
            where.add(cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), startDate));
        if (endDate != null)
            where.add(cb.lessThanOrEqualTo(lendingRoot.get("startDate"), endDate));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(lendingRoot.get("lendingNumber")));

        final TypedQuery<Lending> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
public Lending save(Lending lending) {
    if (lending.getPk() == null) {
        em.persist(lending); 
    } else {
        em.merge(lending); 
    }
    return lending;
}

    @Override
    public void delete(Lending lending) {
        em.remove(em.contains(lending) ? lending : em.merge(lending));
    }

    @Override
    public Optional<Genre> findByReaderDetails(ReaderDetails reader) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByReaderDetails'");
    }
}
