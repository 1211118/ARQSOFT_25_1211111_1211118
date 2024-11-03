package pt.psoft.g1.psoftg1.bookmanagement.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.services.*;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.ArrayList;
import java.util.List;
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private FileStorageService fileStorageService; // Mock for file storage service

    @Mock
    private BookViewMapper bookViewMapper; // Mock for book view mapper

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }


    @Test
    void findByIsbn_shouldReturnBookView_whenBookExists() throws Exception {
        String isbn = "1234567890123";
        Book mockBook = mock(Book.class);
        when(bookService.findByIsbn(isbn)).thenReturn(mockBook);
        // Mock the method to convert to BookView
        // when(bookViewMapper.toBookView(mockBook)).thenReturn(mockBookView);
        when(mockBook.getVersion()).thenReturn(1L);

        mockMvc.perform(get("/api/books/{isbn}", isbn))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookPhoto_shouldReturnNotFound_whenNoPhotoExists() throws Exception {
        String isbn = "1234567890123";
        Book mockBook = mock(Book.class);
        when(bookService.findByIsbn(isbn)).thenReturn(mockBook);
        when(mockBook.getPhoto()).thenReturn(null); // No photo

        mockMvc.perform(delete("/api/books/{isbn}/photo", isbn))
                .andExpect(status().isNotFound());
    }








}
