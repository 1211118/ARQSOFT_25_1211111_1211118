package pt.psoft.g1.psoftg1.genremanagement.services;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import static org.junit.jupiter.api.Assertions.*;

class GenreLendingsDTOTest {

    @Test
    void testConstructorWithDoubleValue() {
        GenreLendingsDTO dto = new GenreLendingsDTO("Fiction", 12.345);
        assertEquals("Fiction", dto.getGenre(), "Genre should be 'Fiction'");
        assertEquals(12.3, dto.getValue(), "Value should be formatted to 12.3");
    }

    @Test
    void testConstructorWithLongValue() {
        GenreLendingsDTO dto = new GenreLendingsDTO("Fantasy", 42L);
        assertEquals("Fantasy", dto.getGenre(), "Genre should be 'Fantasy'");
        assertEquals(42L, dto.getValue(), "Value should be 42");
    }

    @Test
    void testConstructorWithNullDoubleValue() {
        GenreLendingsDTO dto = new GenreLendingsDTO("Mystery", (Double) null);
        assertEquals("Mystery", dto.getGenre(), "Genre should be 'Mystery'");
        assertEquals(0, dto.getValue(), "Value should be 0 when input is null");
    }

    @Test
    void testConstructorWithGenreObjectAndDoubleValue() {
        Genre genre = new Genre("Horror");
        GenreLendingsDTO dto = new GenreLendingsDTO(genre, 18.756);
        assertEquals("Horror", dto.getGenre(), "Genre should be 'Horror'");
        assertEquals(18.8, dto.getValue(), "Value should be formatted to 18.8");
    }

    @Test
    void testConstructorWithGenreObjectAndLongValue() {
        Genre genre = new Genre("Science Fiction");
        GenreLendingsDTO dto = new GenreLendingsDTO(genre, 78L);
        assertEquals("Science Fiction", dto.getGenre(), "Genre should be 'Science Fiction'");
        assertEquals(78L, dto.getValue(), "Value should be 78");
    }

    @Test
    void testFormattingOfDoubleValue() {
        GenreLendingsDTO dto = new GenreLendingsDTO("Drama", 99.999);
        assertEquals(100.0, dto.getValue(), "Value should be rounded to 100.0");
    }

    
}
