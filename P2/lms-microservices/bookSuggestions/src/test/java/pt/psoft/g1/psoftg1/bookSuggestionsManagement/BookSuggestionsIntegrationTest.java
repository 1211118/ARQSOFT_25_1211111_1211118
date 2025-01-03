/*package test.java.pt.psoft.g1.psoftg1.bookSuggestionsManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockBean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pt.psoft.g1.psoftg1.bookSuggestionManagement.api.BookSuggestionController;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.services.BookSuggestionService;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.publishers.BookSuggestionPublisher;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.api.BookSuggestionView;

import java.util.List;


@SpringBootTest
class BookSuggestionIntegrationTest {

    @Autowired
    private BookSuggestionController bookSuggestionController;

    @MockBean
    private BookSuggestionService bookSuggestionService;

    @MockBean
    private BookSuggestionPublisher bookSuggestionPublisher;

    @MockBean
    private FileStorageService fileStorageService;

    private MockMultipartFile mockPhoto;
    private BookSuggestionView mockBookSuggestionView;

    @BeforeEach
    void setUp() {
        // Setup test data
        mockPhoto = new MockMultipartFile(
            "photo",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        mockBookSuggestionView = new BookSuggestionView();
        mockBookSuggestionView.setId(1L);
        mockBookSuggestionView.setTitle("Test Book");
        mockBookSuggestionView.setGenre("Fiction");
        mockBookSuggestionView.setAuthors(List.of(1L, 2L));
        mockBookSuggestionView.setIsbn("1234567890");
        mockBookSuggestionView.setSuggestedByReaderId(1L);
        mockBookSuggestionView.setStatus("PENDING");
    }

    @Test
    void whenSuggestBook_thenSuccess() {
        // Arrange
        when(bookSuggestionService.suggestBook(any()))
            .thenReturn(mockBookSuggestionView);

        // Act
        ResponseEntity<BookSuggestionView> response = bookSuggestionController.suggestBook(
            "Test Book",
            "Fiction",
            List.of(1L, 2L),
            "Test Description",
            mockPhoto,
            1L
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Book", response.getBody().getTitle());
        verify(bookSuggestionService).suggestBook(any());
    }

    @Test
    void whenSuggestBookWithInvalidData_thenBadRequest() {
        // Arrange
        when(bookSuggestionService.suggestBook(any()))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            bookSuggestionController.suggestBook(
                "",  // invalid title
                "Fiction",
                List.of(1L, 2L),
                "Test Description",
                mockPhoto,
                1L
            )
        );
    }

    @Test
    void whenPublishBookSuggestion_thenSuccess() {
        // Arrange
        doNothing().when(bookSuggestionPublisher)
            .publishBookSuggestion(any(BookSuggestionView.class));

        // Act
        bookSuggestionPublisher.publishBookSuggestion(mockBookSuggestionView);

        // Assert
        verify(bookSuggestionPublisher).publishBookSuggestion(mockBookSuggestionView);
    }

    /* 
    @Test
    void whenReceiveBookSuggestion_thenSuccess() {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        BookSuggestionEventRabbitmqReceiver receiver = new BookSuggestionEventRabbitmqReceiver();
        String json = objectMapper.writeValueAsString(mockBookSuggestionView);
        Message message = MessageBuilder
            .withBody(json.getBytes())
            .build();

        // Act
        receiver.receiveBookSuggestionCreated(message);

        // Assert - Verify no exceptions are thrown and message is processed
        // You could add more specific assertions based on your requirements
    }

}

*/