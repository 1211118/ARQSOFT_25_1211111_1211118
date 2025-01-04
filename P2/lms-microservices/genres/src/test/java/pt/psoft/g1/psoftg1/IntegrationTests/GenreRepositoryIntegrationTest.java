package pt.psoft.g1.psoftg1.IntegrationTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.publishers.GenreEventsPublisher;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@SpringBootTest
class GenreRepositoryIntegrationTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @MockBean
    private GenreEventsPublisher genreEventsPublisher;

    @Test
    void shouldSaveAndRetrieveGenre() {
    
    Genre genre = new Genre("Mystery");
    genreRepository.save(genre);

    Optional<Genre> retrievedGenre = genreRepository.findByString("Mystery");

    assertTrue(retrievedGenre.isPresent());
    assertEquals("Mystery", retrievedGenre.get().getGenre());
    }

    @Test
    void shouldThrowErrorWhenSavingDuplicateGenre() {
    
    genreRepository.save(new Genre("Duplicate"));

    assertThrows(DataIntegrityViolationException.class, () -> {
        genreRepository.save(new Genre("Duplicate"));
    });}


    

}