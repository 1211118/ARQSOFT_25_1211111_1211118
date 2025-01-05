package pt.psoft.g1.psoftg1.bookmanagement.api;

import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookSuggestionView;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class BookSuggestionEventRabbitmqReceiver {

    private AuthorService authorService;
    private GenreService genreService;
    private BookService bookService;
    private CreateAuthorRequest createAuthorRequest;

    @Autowired
    public BookSuggestionEventRabbitmqReceiver(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @RabbitListener(queues = "#{autoDeleteQueue_BookSuggestion_Created.name}")
    public void receiveBookSuggestionCreated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookSuggestionView bookSuggestionView = objectMapper.readValue(jsonReceived, BookSuggestionView.class);

            System.out.println(" [x] Received Book Suggestion Created: " + bookSuggestionView);

            // 1. Handle Genre
            Genre genre = genreService.validateGenre(bookSuggestionView.getGenre());

            // 2. Handle Authors
            List<Author> bookAuthors = authorService.validateAuthors(bookSuggestionView.getAuthors());

            // 3. Create BookView
            BookViewAMQP bookViewAMQP = bookService.toBookViewAMQP(bookSuggestionView, genre, bookAuthors);

            // 4. Create Book
            bookService.create(bookViewAMQP);

            System.out.println(" [x] Book created successfully: " + bookSuggestionView.getTitle());

        } catch(Exception ex) {
            System.out.println(" [x] Exception receiving book suggestion created event: '" + ex.getMessage() + "'");
        }
    }

}
