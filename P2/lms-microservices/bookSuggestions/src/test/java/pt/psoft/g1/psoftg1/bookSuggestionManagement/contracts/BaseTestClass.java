
/*
package pt.psoft.g1.psoftg1.bookSuggestionManagement.contracts;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.springframework.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ActiveProfiles;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.api.BookSuggestionView;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.config.TestConfig;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.infrastructure.publishers.impl.BookSuggestionRabbitmqPublisherImpl;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.services.BookSuggestionService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMessageVerifier
@Import(TestConfig.class)
public abstract class BaseTestClass {

    

    @Autowired
    private BookSuggestionService bookSuggestionService;

    @Autowired
    private BookSuggestionRabbitmqPublisherImpl publisher;

    @Autowired
    @Qualifier("LMS.book.suggestions")
    private PollableChannel bookSuggestionsChannel;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(bookSuggestionService);
    }

    public void publishBookSuggestion() {

    
        BookSuggestionView suggestion = BookSuggestionView.builder()
            .title("Test Book")
            .description("Test Description")
            .genre("Fiction")
            .photoURI("http://example.com/photo.jpg")
            .authors(List.of("Author 1", "Author 2"))
            .isbn("058116685X")
            .suggestedByReaderId(1L)
            .status("PENDING")
            .build();
    
        System.out.println("Publishing message: " + suggestion);
    
        publisher.publishBookSuggestion(suggestion);
    
        // Espera a mensagem ser consumida do canal
        Message<?> message = null;
        try {
            message = bookSuggestionsChannel.receive(5000); // Aguarda até 5 segundos
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        assertNotNull(message, "Message was not received on the channel");
        System.out.println("Message received: " + message.getPayload());
    
        BookSuggestionView receivedSuggestion = (BookSuggestionView) message.getPayload();
        assertEquals("Test Book", receivedSuggestion.getTitle());

        
    }

    

    
}
*/