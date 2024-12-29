package pt.psoft.g1.psoftg1.bookmanagement.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateBook_NotFound() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setIsbn("1234567890");

        when(bookRepository.findByIsbn("1234567890")).thenThrow(new NotFoundException(Book.class, "1234567890"));

        assertThrows(NotFoundException.class, () -> {
            bookService.update(request, "1");
        });
    }


    @Test
    void testRemoveBookPhoto_NotFound() {
        when(bookRepository.findByIsbn("1234567890")).thenThrow(new NotFoundException(Book.class, "1234567890"));

        assertThrows(NotFoundException.class, () -> {
            bookService.removeBookPhoto("1234567890", 1L);
        });
    }


    @Test
    void testFindByIsbn_NotFound() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookService.findByIsbn("1234567890");
        });
    }

}
