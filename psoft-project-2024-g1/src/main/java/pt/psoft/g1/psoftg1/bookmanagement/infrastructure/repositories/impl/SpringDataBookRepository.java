package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class SpringDataBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b WHERE b.isbn.isbn = :isbn", Book.class
        );
        query.setParameter("isbn", isbn);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        TypedQuery<BookCountDTO> query = em.createQuery(
            "SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO(b, COUNT(l)) " +
            "FROM Book b JOIN Lending l ON l.book = b " +
            "WHERE l.startDate > :oneYearAgo GROUP BY b ORDER BY COUNT(l) DESC",
            BookCountDTO.class
        );
        query.setParameter("oneYearAgo", oneYearAgo);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new org.springframework.data.domain.PageImpl<>(query.getResultList(), pageable, 5);
    }

    @Override
    public List<Book> findByGenre(String genre) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b WHERE b.genre.genre LIKE :genre", Book.class
        );
        query.setParameter("genre", "%" + genre + "%");
        return query.getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b WHERE b.title.title LIKE :title", Book.class
        );
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b JOIN b.authors a WHERE a.name LIKE :authorName", Book.class
        );
        query.setParameter("authorName", "%" + authorName + "%");
        return query.getResultList();
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber", Book.class
        );
        query.setParameter("authorNumber", authorNumber);
        return query.getResultList();
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page pageable, SearchBooksQuery query) {
    final CriteriaBuilder cb = em.getCriteriaBuilder();
    final CriteriaQuery<Book> cq = cb.createQuery(Book.class);
    final Root<Book> root = cq.from(Book.class);
    final Join<Book, Genre> genreJoin = root.join("genre");
    final Join<Book, Author> authorJoin = root.join("authors");
    cq.select(root);

    final List<Predicate> where = new ArrayList<>();

    // Add filters based on the SearchBooksQuery fields
    if (StringUtils.hasText(query.getTitle())) {
        where.add(cb.like(root.get("title").get("title"), "%" + query.getTitle() + "%"));
    }

    if (StringUtils.hasText(query.getGenre())) {
        where.add(cb.like(genreJoin.get("genre"), "%" + query.getGenre() + "%"));
    }

    if (StringUtils.hasText(query.getAuthorName())) {
        where.add(cb.like(authorJoin.get("name"), "%" + query.getAuthorName() + "%"));
    }

    // Apply the conditions
    cq.where(where.toArray(new Predicate[0]));
    cq.orderBy(cb.asc(root.get("title")));  // Order by title alphabetically

    // Create the query and set pagination
    TypedQuery<Book> q = em.createQuery(cq);
    q.setFirstResult((pageable.getNumber()) * pageable.getPageSize());
    q.setMaxResults(pageable.getPageSize());

    return q.getResultList();
}

@Override
public Book save(Book book) {
    if (book.getPk() == 0) {  // Verifica se a chave primária ainda não foi atribuída (novo livro)
        em.persist(book);
    } else {
        em.merge(book);  // Atualiza o livro se já tiver sido persistido anteriormente
    }
    return book;
}

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

}
