package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IsbnTest {

    @Test
    void ensureIsbnMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn(null));
    }

    @Test
    void ensureIsbnMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn(""));
    }

    @Test
    void ensureIsbnMustNotBeOversize() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn("A".repeat(21))); // Testa um ISBN com 21 caracteres
    }

    @Test
    void ensureIsbnMustNotBeUndersize() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn("123456789")); // Testa um ISBN com 9 caracteres
    }

    @Test
    void ensureIsbnMustNotContainInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn("978-3-16-148410-0")); // Testa caracteres não permitidos
        assertThrows(IllegalArgumentException.class, () -> new Isbn("978#3161484100")); // Testa ISBN com caracteres inválidos
    }

    @Test
    void ensureIsbn13IsSet() {
        final var isbn = new Isbn("9782826012092");
        assertEquals("9782826012092", isbn.toString());
    }

    @Test
    void ensureChecksum13IsCorrect() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn("9782826012099")); // Testa um ISBN 13 com checksum incorreto
    }

    @Test
    void ensureIsbn10IsSet() {
        final var isbn = new Isbn("8175257660");
        assertEquals("8175257660", isbn.toString());
    }

    @Test
    void ensureChecksum10IsCorrect() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn("8175257667")); // Testa um ISBN 10 com checksum incorreto
    }

    @Test
    void ensureIsbn10AllowsOnlyValidIsbn10Format() {
        assertDoesNotThrow(() -> new Isbn("123456789X")); // Testa um ISBN 10 válido com 'X' como último dígito
    }

    @Test
    void ensureToStringReturnsCorrectFormat() {
        Isbn isbn13 = new Isbn("9781234567897");
        Isbn isbn10 = new Isbn("123456789X");

        assertEquals("9781234567897", isbn13.toString());
        assertEquals("123456789X", isbn10.toString());
    }

    @Test
    void ensureDifferentIsbnInstancesAreEqual() {
        Isbn isbn1 = new Isbn("9781234567897");
        Isbn isbn2 = new Isbn("9781234567897");

        assertEquals(isbn1, isbn2); // Verifica se duas instâncias de ISBN com o mesmo valor são iguais
    }

    @Test
    void ensureIsbnHashCodeIsConsistentWithEquals() {
        Isbn isbn1 = new Isbn("9781234567897");
        Isbn isbn2 = new Isbn("9781234567897");

        assertEquals(isbn1.hashCode(), isbn2.hashCode()); // Verifica se o hash code é igual para ISBNs iguais
    }
}
