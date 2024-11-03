package pt.psoft.g1.psoftg1.genremanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(GenreServiceImpl.class)
class GenreServiceIntegrationTest {

    @Autowired
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genreService.save(new Genre("Fiction"));
        genreService.save(new Genre("Fantasy"));
        genreService.save(new Genre("Science Fiction"));
        genreService.save(new Genre("Mystery"));
        genreService.save(new Genre("Horror"));
    }

    @Test
    void testFindAllGenres() {
        Iterable<Genre> genres = genreService.findAll();
        assertNotNull(genres, "Genres should not be null");
        assertEquals(5, ((List<Genre>) genres).size(), "There should be 5 genres in the database");
    }

    @Test
    void testFindByString() {
        Optional<Genre> genre = genreService.findByString("Fiction");
        assertTrue(genre.isPresent(), "Genre 'Fiction' should be found");
        assertEquals("Fiction", genre.get().getGenre(), "Genre name should match 'Fiction'");
    }

    @Test
    void testFindByStringWithNonExistentGenre() {
        Optional<Genre> genre = genreService.findByString("NonExistentGenre");
        assertFalse(genre.isPresent(), "Non-existent genre should not be found");
    }

    @Test
    void testSaveGenre() {
        Genre newGenre = new Genre("Romance");
        Genre savedGenre = genreService.save(newGenre);
        
        assertNotNull(savedGenre, "Saved genre should not be null");
        assertEquals("Romance", savedGenre.getGenre(), "Saved genre should match 'Romance'");
    }

    @Test
    void testFindTopGenreByBooks() {
        List<GenreBookCountDTO> topGenres = genreService.findTopGenreByBooks();
        
        assertNotNull(topGenres, "Top genres by books should not be null");
        assertTrue(topGenres.size() <= 5, "The result should contain at most 5 genres");
    }

    @Test
    void testGetAverageLendingsWithValidQuery() {
        GetAverageLendingsQuery query = new GetAverageLendingsQuery(2023, 10);
        Page page = new Page(1, 5);
        
        List<GenreLendingsDTO> averageLendings = genreService.getAverageLendings(query, page);
        
        assertNotNull(averageLendings, "Average lendings should not be null");
        assertTrue(averageLendings.size() >= 0, "The size should be 0 or more based on data presence");
    }

    @Test
    void testGetAverageLendingsWithNullPage() {
        GetAverageLendingsQuery query = new GetAverageLendingsQuery(2023, 10);
        
        List<GenreLendingsDTO> averageLendings = genreService.getAverageLendings(query, null);
        
        assertNotNull(averageLendings, "Average lendings should not be null even when page is null");
    }

    @Test
    void testGetLendingsPerMonthLastYearByGenre() {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = genreService.getLendingsPerMonthLastYearByGenre();
        
        assertNotNull(lendingsPerMonth, "Lendings per month last year by genre should not be null");
        assertTrue(lendingsPerMonth.size() >= 0, "The size should be 0 or more based on data presence");
    }

    @Test
    void testGetLendingsAverageDurationPerMonthWithEmptyResults() {
        String startDate = "1900-01-01";
        String endDate = "1900-12-31";
        
        NotFoundException exception = assertThrows(NotFoundException.class, () -> genreService.getLendingsAverageDurationPerMonth(startDate, endDate));
        assertEquals("No objects match the provided criteria", exception.getMessage());
    }

    @Test
    void testGetLendingsAverageDurationPerMonthWithInvalidDates() {
        String startDate = "invalid-date";
        String endDate = "another-invalid-date";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> genreService.getLendingsAverageDurationPerMonth(startDate, endDate));
        assertEquals("Expected format is YYYY-MM-DD", exception.getMessage());
    }

    @Test
    void testGetLendingsAverageDurationPerMonthWithStartDateAfterEndDate() {
        String startDate = "2023-10-10";
        String endDate = "2023-01-01";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> genreService.getLendingsAverageDurationPerMonth(startDate, endDate));
        assertEquals("Start date cannot be after end date", exception.getMessage());
    }
}
