package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookConverter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MongoBookRepository implements BookRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
public Optional<Book> findByIsbn(String isbn) {
    Query query = new Query();
    query.addCriteria(Criteria.where("isbn.isbn").is(isbn));
    BookMongo bookMongo = mongoTemplate.findOne(query, BookMongo.class);

    // Verifica se bookMongo é null e retorna Optional.empty() caso não encontre o documento
    if (bookMongo == null) {
        return Optional.empty();
    }

    return Optional.of(BookConverter.toEntity(bookMongo));
}

    @Override
    public List<Book> findByGenre(String genre) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre.genre").is(genre));
        List<BookMongo> booksMongo = mongoTemplate.find(query, BookMongo.class);
        return booksMongo.stream()
                .map(BookConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title.title").regex(title, "i")); // Case insensitive search
        List<BookMongo> booksMongo = mongoTemplate.find(query, BookMongo.class);
        return booksMongo.stream()
                .map(BookConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.name").regex(authorName, "i")); // Case insensitive search
        List<BookMongo> booksMongo = mongoTemplate.find(query, BookMongo.class);
        return booksMongo.stream()
                .map(BookConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        // Implementação da lógica de agregação no MongoDB
        return Page.empty();
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.authorNumber").is(authorNumber));
        List<BookMongo> booksMongo = mongoTemplate.find(query, BookMongo.class);
        return booksMongo.stream()
                .map(BookConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page pageable, SearchBooksQuery query) {
        Query mongoQuery = new Query();

        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("title.title").regex(query.getTitle(), "i"));
        }
        if (query.getGenre() != null && !query.getGenre().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("genre.genre").regex(query.getGenre(), "i"));
        }
        if (query.getAuthorName() != null && !query.getAuthorName().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("authors.name").regex(query.getAuthorName(), "i"));
        }

        // Paginação
        mongoQuery.skip((pageable.getNumber() - 1) * pageable.getPageSize()).limit(pageable.getPageSize());

        List<BookMongo> booksMongo = mongoTemplate.find(mongoQuery, BookMongo.class);
        return booksMongo.stream()
                .map(BookConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Book save(Book book) {
        BookMongo bookMongo = BookConverter.toMongo(book);
        BookMongo savedBookMongo = mongoTemplate.save(bookMongo);
        return BookConverter.toEntity(savedBookMongo);
    }

    @Override
    public void delete(Book book) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isbn.isbn").is(book.getIsbn()));
        mongoTemplate.remove(query, BookMongo.class);
    }
}
