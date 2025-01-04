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
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.publishers.GenreEventsPublisher;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@SpringBootTest
class GenreServiceIntegrationTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @MockBean
    private GenreEventsPublisher genreEventsPublisher;

    @Test
    void shouldPublishGenreCreatedEvent() {
        Genre genre = new Genre("Fantasy");
        genreService.save(genre);

        verify(genreEventsPublisher, Mockito.times(1)).sendGenreCreated(Mockito.any(Genre.class));
    }

    @Test
    void shouldSaveGenreAndPublishEvent() {
    
    Genre genre = new Genre("Science Fiction");

    Genre savedGenre = genreService.save(genre);

    Optional<Genre> retrievedGenre = genreRepository.findByString("Science Fiction");
    assertTrue(retrievedGenre.isPresent());
    assertEquals("Science Fiction", retrievedGenre.get().getGenre());

    verify(genreEventsPublisher, times(1)).sendGenreCreated(savedGenre);
    }

    @Test
    void shouldReturnAllGenres() {

    genreService.save(new Genre("Adventure"));
    genreService.save(new Genre("Fantasy"));

    Iterable<Genre> genres = genreService.findAll();

    List<Genre> genreList = new ArrayList<>();
    genres.forEach(genreList::add);

    assertEquals(2, genreList.size());
    assertTrue(genreList.stream().anyMatch(g -> g.getGenre().equals("Adventure")));
    assertTrue(genreList.stream().anyMatch(g -> g.getGenre().equals("Fantasy")));
    }

    @Test
    void shouldFindGenreByName() {
    
    genreService.save(new Genre("Horror"));

    Optional<Genre> genre = genreService.findByString("Horror");

    assertTrue(genre.isPresent());
    assertEquals("Horror", genre.get().getGenre());
    }

    @Test
    void shouldReturnEmptyForNonexistentGenre() {
    Optional<Genre> genre = genreService.findByString("Nonexistent");

    assertTrue(genre.isEmpty());
    }

}
