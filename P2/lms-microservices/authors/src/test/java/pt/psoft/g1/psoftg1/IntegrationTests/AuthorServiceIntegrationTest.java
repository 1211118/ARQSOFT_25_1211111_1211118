package pt.psoft.g1.psoftg1.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.SpringDataAuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.publishers.AuthorEventsPublisher;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private SpringDataAuthorRepository authorRepository;

    @MockBean
    private AuthorEventsPublisher authorEventsPublisher;

    @Test
    void shouldCreateAuthorAndPublishEvent() {
        CreateAuthorRequest request = new CreateAuthorRequest();
        request.setName("Neil Gaiman");
        request.setBio("Author of American Gods");

        Author createdAuthor = authorService.create(request);

        assertNotNull(createdAuthor.getAuthorNumber());
        assertEquals("Neil Gaiman", createdAuthor.getName());

        verify(authorEventsPublisher, times(1)).sendAuthorCreated(createdAuthor);
    }

    @Test
    void shouldRemoveAuthorPhoto() {
        Author author = new Author("Author with Photo", "Bio", "photo_uri");
        Author savedAuthor = authorRepository.save(author);

        authorService.removeAuthorPhoto(savedAuthor.getAuthorNumber(), savedAuthor.getVersion());

        Optional<Author> updatedAuthor = authorRepository.findByAuthorNumber(savedAuthor.getAuthorNumber());
        assertTrue(updatedAuthor.isPresent());
        assertNull(updatedAuthor.get().getPhoto());
    }

    @Test
    void shouldHandleEmptyResultForCoAuthors() {
        Author author = authorRepository.save(new Author("Solo Author", "Bio", null));

        List<Author> coAuthors = authorService.findCoAuthorsByAuthorNumber(author.getAuthorNumber());

        assertNotNull(coAuthors);
        assertTrue(coAuthors.isEmpty());
    }

}
