package pt.psoft.g1.psoftg1.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.SpringDataAuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorRepositoryIntegrationTest {

    @Autowired
    private SpringDataAuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSearchAuthorsByName() {
        authorRepository.save(new Author("Mark Twain", "Author of Adventures of Tom Sawyer", null));
        authorRepository.save(new Author("Markus Zusak", "Author of The Book Thief", null));

        List<Author> authors = authorRepository.searchByNameNameStartsWith("Mark");

        assertEquals(2, authors.size());
    }

    @Test
    void shouldFindAllAuthors() {
        // Setup
        authorRepository.save(new Author("Author 1", "Bio 1", null));
        authorRepository.save(new Author("Author 2", "Bio 2", null));

        // Test
        Iterable<Author> authors = authorRepository.findAll();
        long count = authors.spliterator().getExactSizeIfKnown();
        assertTrue(count >= 2);
    }

    @Test
    void shouldFindAuthorsByExactName() {
        // Setup
        authorRepository.save(new Author("Exact Name", "Bio", null));

        // Test
        List<Author> authors = authorRepository.searchByNameName("Exact Name");
        assertEquals(1, authors.size());
        assertEquals("Exact Name", authors.get(0).getName());
    }

    @Test
    void shouldReturnEmptyWhenSearchingForNonexistentAuthor() {
        List<Author> authors = authorRepository.searchByNameName("Nonexistent Author");
        assertNotNull(authors);
        assertTrue(authors.isEmpty());
    }

    @Test
    void shouldDeleteAuthor() {
        // Setup
        Author author = authorRepository.save(new Author("Author to Delete", "Bio", null));
        Long authorId = author.getAuthorNumber();

        // Action
        authorRepository.delete(author);

        // Test
        assertTrue(authorRepository.findByAuthorNumber(authorId).isEmpty());
    }

    
}


