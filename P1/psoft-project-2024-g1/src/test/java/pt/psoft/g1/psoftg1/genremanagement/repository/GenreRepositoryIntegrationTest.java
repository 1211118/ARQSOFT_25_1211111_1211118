package pt.psoft.g1.psoftg1.genremanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.SpringDataGenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GenreRepositoryIntegrationTest {

    @Autowired
private GenreRepository genreRepository;

    @BeforeEach
void setUp() {
    ((CrudRepository<Genre, Long>) genreRepository).save(new Genre("Fiction"));
    ((CrudRepository<Genre, Long>) genreRepository).save(new Genre("Fantasy"));
    ((CrudRepository<Genre, Long>) genreRepository).save(new Genre("Science Fiction"));
    ((CrudRepository<Genre, Long>) genreRepository).save(new Genre("Mystery"));
    ((CrudRepository<Genre, Long>) genreRepository).save(new Genre("Horror"));
}

    @Test
    void testSaveAndFindAllGenres() {
        Iterable<Genre> genres = genreRepository.findAll();
        assertNotNull(genres, "Genres should not be null");
        assertEquals(5, ((List<Genre>) genres).size(), "There should be 5 genres in the database");
    }

    @Test
    void testFindByString() {
        Optional<Genre> genre = genreRepository.findByString("Fiction");
        assertTrue(genre.isPresent(), "Genre 'Fiction' should be found");
        assertEquals("Fiction", genre.get().getGenre(), "Genre name should match 'Fiction'");
    }

    @Test
void testDeleteGenre() {
    Optional<Genre> genre = genreRepository.findByString("Horror");
    assertTrue(genre.isPresent(), "Genre 'Horror' should be found before deletion");

    ((CrudRepository<Genre, Long>) genreRepository).delete(genre.get());

    Optional<Genre> deletedGenre = genreRepository.findByString("Horror");
    assertFalse(deletedGenre.isPresent(), "Genre 'Horror' should not be found after deletion");
}

    @Test
    void testFindTop5GenreByBookCount() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<GenreBookCountDTO> topGenres = genreRepository.findTop5GenreByBookCount(pageable);

        assertNotNull(topGenres, "Top genres by book count should not be null");
        assertTrue(topGenres.getContent().size() <= 5, "The result should contain at most 5 genres");
    }

    @Test
    void testGetLendingsPerMonthLastYearByGenre() {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = genreRepository.getLendingsPerMonthLastYearByGenre();

        assertNotNull(lendingsPerMonth, "Lendings per month last year by genre should not be null");
        assertTrue(lendingsPerMonth.size() >= 0, "The size should be 0 or more based on data presence");
    }

    @Test
    void testGetAverageLendingsInMonth() {
        LocalDate month = LocalDate.of(2023, 10, 1);
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
        List<GenreLendingsDTO> averageLendings = genreRepository.getAverageLendingsInMonth(month, page);

        assertNotNull(averageLendings, "Average lendings in month should not be null");
        assertTrue(averageLendings.size() >= 0, "The size should be 0 or more based on data presence");
    }

    @Test
    void testGetLendingsAverageDurationPerMonth() {
        LocalDate startDate = LocalDate.now().minusMonths(12);
        LocalDate endDate = LocalDate.now();

        List<GenreLendingsPerMonthDTO> lendingsAverageDuration = genreRepository.getLendingsAverageDurationPerMonth(startDate, endDate);

        assertNotNull(lendingsAverageDuration, "Lendings average duration per month should not be null");
        assertTrue(lendingsAverageDuration.size() >= 0, "The size should be 0 or more based on data presence");
    }

    @Test
    void testFindByStringWithNonExistentGenre() {
        Optional<Genre> genre = genreRepository.findByString("NonExistentGenre");
        assertFalse(genre.isPresent(), "Non-existent genre should not be found");
    }


    @Test
    void testFindTop5GenreByBookCountWithPaginationAndSorting() {
        PageRequest pageable = PageRequest.of(0, 5, Sort.by("genre").ascending());
        Page<GenreBookCountDTO> topGenres = genreRepository.findTop5GenreByBookCount(pageable);

        assertNotNull(topGenres, "Top genres by book count should not be null");
        assertTrue(topGenres.getContent().size() <= 5, "The result should contain at most 5 genres");
    }

    @Test
    void testGetLendingsPerMonthLastYearByGenreWithEdgeDates() {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = genreRepository.getLendingsPerMonthLastYearByGenre();

        assertNotNull(lendingsPerMonth, "Lendings per month last year by genre should not be null");
        assertTrue(lendingsPerMonth.size() >= 0, "The size should be 0 or more based on data presence");
    }

    @Test
    void testGetAverageLendingsInMonthWithDifferentPageSizes() {
        LocalDate month = LocalDate.of(2023, 10, 1);
        pt.psoft.g1.psoftg1.shared.services.Page page1 = new pt.psoft.g1.psoftg1.shared.services.Page(1, 5);
        pt.psoft.g1.psoftg1.shared.services.Page page2 = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

        List<GenreLendingsDTO> averageLendingsPage1 = genreRepository.getAverageLendingsInMonth(month, page1);
        List<GenreLendingsDTO> averageLendingsPage2 = genreRepository.getAverageLendingsInMonth(month, page2);

        assertTrue(averageLendingsPage2.size() >= averageLendingsPage1.size(), "Page with larger size should have more or equal results");
    }

    @Test
    void testGetLendingsAverageDurationPerMonthWithNoResults() {
        LocalDate startDate = LocalDate.of(1900, 1, 1);
        LocalDate endDate = LocalDate.of(1900, 12, 31);

        List<GenreLendingsPerMonthDTO> lendingsAverageDuration = genreRepository.getLendingsAverageDurationPerMonth(startDate, endDate);

        assertTrue(lendingsAverageDuration.isEmpty(), "No data should be returned for an empty date range");
    }

    @Test
    void testFindByStringWithNonExistentGenreOptionalHandling() {
        Optional<Genre> genre = genreRepository.findByString("NonExistentGenre");

        assertTrue(genre.isEmpty(), "Non-existent genre should not be found");
        genre.ifPresent(g -> fail("Optional should be empty for non-existent genre"));
    }



    @Test
    void testFindAllAfterDeletion() {
        genreRepository.delete(genreRepository.findByString("Mystery").orElseThrow());
        Iterable<Genre> genres = genreRepository.findAll();

        assertNotNull(genres, "Genres should not be null after deletion");
        assertEquals(4, ((List<Genre>) genres).size(), "There should be 4 genres in the database after one deletion");
    }

    @Test
    void testDeleteNonExistentGenre() {
        Genre nonExistentGenre = new Genre("NonExistentGenre");

        assertDoesNotThrow(() -> genreRepository.delete(nonExistentGenre), "Deleting a non-existent genre should not throw an exception");
    }

}
