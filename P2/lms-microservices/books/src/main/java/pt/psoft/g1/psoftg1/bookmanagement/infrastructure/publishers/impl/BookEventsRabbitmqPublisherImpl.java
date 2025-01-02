package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.psoft.g1.psoftg1.bookmanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;

@Service
@RequiredArgsConstructor
public class BookEventsRabbitmqPublisherImpl implements BookEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final BookViewAMQPMapper bookViewAMQPMapper;

    private int count = 0;

    @Override
    public void sendBookCreated(Book book) {
        sendBookEvent(book, book.getVersion(), BookEvents.BOOK_CREATED);
    }

    @Override
    public void sendBookUpdated(Book book, Long currentVersion) {
        sendBookEvent(book, currentVersion, BookEvents.BOOK_UPDATED);
    }

    @Override
    public void sendBookDeleted(Book book, Long currentVersion) {
        sendBookEvent(book, currentVersion, BookEvents.BOOK_DELETED);
    }

    public void sendBookEvent(Book book, Long currentVersion, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            BookViewAMQP bookViewAMQP = bookViewAMQPMapper.toBookViewAMQP(book);
            bookViewAMQP.setVersion(currentVersion);

            String jsonString = objectMapper.writeValueAsString(bookViewAMQP);

            this.template.convertAndSend(direct.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book event: '" + ex.getMessage() + "'");
        }
    }

    @Override
    public void sendAuthorCreated(AuthorViewAMQP author) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(author);
            this.template.convertAndSend(direct.getName(), "AUTHOR_CREATED", message);
            System.out.println(" [x] Sent AUTHOR_CREATED event: " + message);
        } catch (Exception e) {
            System.err.println(" [!] Failed to send AUTHOR_CREATED event: " + e.getMessage());
        }
    }

    @Override
    public void sendGenreCreated(GenreViewAMQP genre) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(genre);
            template.convertAndSend(direct.getName(), "GENRE_CREATED", message);
            System.out.println(" [x] Sent GENRE_CREATED event: " + message);
        } catch (Exception e) {
            System.err.println(" [!] Failed to send GENRE_CREATED event: " + e.getMessage());
        }
    }

}