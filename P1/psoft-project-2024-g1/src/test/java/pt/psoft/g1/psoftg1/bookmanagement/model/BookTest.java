package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final Author validAuthor1 = new Author("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = new Author("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = new Genre("Fantasia");
    private ArrayList<Author> authors;

    @BeforeEach
    void setUp(){
        authors = new ArrayList<>();
        authors.add(validAuthor1); // Default author for most tests
    }

    @Test
    void ensureIsbnNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureTitleNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, null, null, validGenre, authors, null));
    }

    @Test
    void ensureGenreNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null,null, authors, null));
    }

    @Test
    void ensureAuthorsNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, validGenre, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty(){
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, validGenre, new ArrayList<>(), null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authors.add(validAuthor2);
        assertDoesNotThrow(() -> new Book(validIsbn, validTitle, null, validGenre, authors, null));
    }

    // Novos Testes

    // Teste unitário de caixa opaca
    @Test
    void ensureDescriptionCanBeSet() {
        Book book = new Book(validIsbn, validTitle, "Uma descrição válida.", validGenre, authors, null);
        assertEquals("Uma descrição válida.", book.getDescription());
        // Este teste verifica se o método getDescription() retorna a descrição correta.
        // Como a implementação do método não é exposta, este é um teste de caixa opaca.
    }

    // Teste unitário de caixa transparente
    @Test
    void ensureApplyPatchThrowsExceptionForStaleObject() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Novo Título");

        assertThrows(StaleObjectStateException.class, () -> book.applyPatch(999L, request)); // Use an outdated version
        // Este teste verifica se o método applyPatch lança uma exceção quando uma versão desatualizada é usada.
        // Aqui, conhecemos a implementação do método, tornando este teste um de caixa transparente.
    }

    // Teste unitário de caixa opaca
    @Test
    void ensureInvalidIsbnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book("invalidIsbn", validTitle, null, validGenre, authors, null));
        // Este teste garante que uma exceção é lançada ao tentar criar um livro com um ISBN inválido.
        // A lógica interna da classe Book não é exposta, caracterizando um teste de caixa opaca.
    }

    // Teste unitário de caixa opaca
    @Test
    void ensureValidIsbnCreatesBook() {
        assertDoesNotThrow(() -> new Book("9782826012092", validTitle, null, validGenre, authors, null));
        // Este teste verifica se a criação de um livro com um ISBN válido não lança exceções.
        // Novamente, como a implementação não é exposta, é um teste de caixa opaca.
    }

    //Comenta a partir daqui

    @Test
    void ensureDescriptionCannotExceedMaxLength() {
        String longDescription = "a".repeat(4097); // 4096 + 1 characters
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, longDescription, validGenre, authors, null));
    }

    @Test
    void ensureToStringReturnsIsbn() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        assertEquals(validIsbn, book.getIsbn());
    }

    @Test
    void ensureTitleCanBeUpdated() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Título Atualizado");

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request)); // Versão atual deve ser válida
        assertEquals("Título Atualizado", book.getTitle().toString());
    }

    @Test
    void ensureDescriptionCanBeUpdated() {
        Book book = new Book(validIsbn, validTitle, "Descrição Inicial", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setDescription("Nova Descrição");

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request)); // Versão atual deve ser válida
        assertEquals("Nova Descrição", book.getDescription());
    }

    @Test
    void ensureGenreCanBeUpdated() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        Genre newGenre = new Genre("Aventura");
        UpdateBookRequest request = new UpdateBookRequest();
        request.setGenreObj(newGenre);

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request)); // Versão atual deve ser válida
        assertEquals(newGenre, book.getGenre());
    }

    @Test
    void ensureAuthorsCanBeUpdated() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        ArrayList<Author> newAuthors = new ArrayList<>();
        newAuthors.add(new Author("Novo Autor", "Descrição do Novo Autor", null));
        UpdateBookRequest request = new UpdateBookRequest();
        request.setAuthorObjList(newAuthors);

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request)); // Versão atual deve ser válida
        assertEquals(newAuthors, book.getAuthors());
    }

    @Test
    void ensureValidIsbnCanBeCreated() {
        String validIsbn10 = "123456789X"; // ISBN-10 válido
        assertDoesNotThrow(() -> new Book(validIsbn10, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureInvalidIsbnThrowsExceptionOnCreation() {
        String invalidIsbn = "123"; // ISBN inválido
        assertThrows(IllegalArgumentException.class, () -> new Book(invalidIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureBlankTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, "", null, validGenre, authors, null));
    }

    @Test
    void ensureInvalidIsbnLengthThrowsException() {
        String shortIsbn = "123"; // ISBN muito curto
        assertThrows(IllegalArgumentException.class, () -> new Book(shortIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureInvalidIsbnFormatThrowsException() {
        String invalidIsbnFormat = "ISBN-1234567890"; // Formato inválido de ISBN
        assertThrows(IllegalArgumentException.class, () -> new Book(invalidIsbnFormat, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureAuthorsCannotBeNullOnCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, "Descrição", validGenre, null, null));
    }

    @Test
    void ensureUpdatingWithEmptyTitleThrowsException() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("");

        assertThrows(IllegalArgumentException.class, () -> book.applyPatch(book.getVersion(), request));
    }



    @Test
    void ensureUpdateWithValidTitleSucceeds() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Novo Título");

        book.applyPatch(book.getVersion(), request);

        assertEquals("Novo Título", book.getTitle().toString()); // O título deve ser atualizado corretamente
    }

    @Test
    void ensureUpdatingWithDifferentIsbnKeepsOldIsbn() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setIsbn("novo-isbn-123"); // ISBN inválido não deve mudar o original

        book.applyPatch(book.getVersion(), request);

        assertEquals(validIsbn, book.getIsbn()); // O ISBN original deve permanecer inalterado
    }

    // FFGHJGFD


    @Test
    void ensureBookCreationWithNullIsbnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, "Descrição", validGenre, authors, null));
    }

    @Test
    void ensureBookCreationWithEmptyIsbnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book("", validTitle, "Descrição", validGenre, authors, null));
    }

    @Test
    void ensureBookCreationWithNullTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, null, "Descrição", validGenre, authors, null));
    }

    @Test
    void ensureBookCreationWithEmptyTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, "", "Descrição", validGenre, authors, null));
    }

    @Test
    void ensureBookCreationWithNullGenreThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, "Descrição", null, authors, null));
    }

    @Test
    void ensureBookCreationWithNullAuthorsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, "Descrição", validGenre, null, null));
    }

    @Test
    void ensureBookCreationWithEmptyAuthorsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, "Descrição", validGenre, new ArrayList<>(), null));
    }

    @Test
    void ensureUpdatingWithSameAuthorsDoesNotThrowException() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setAuthorObjList(authors); // Mesma lista de autores

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request)); // Não deve lançar exceção
    }

    @Test
    void ensureBookCreationWithWhitespaceIsbnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book("   ", validTitle, "Descrição", validGenre, authors, null));
    }

    @Test
    void ensureBookCreationWithWhitespaceTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, "   ", "Descrição", validGenre, authors, null));
    }


    // Teste de mutação para ISBN com caracteres alfanuméricos inválidos
    @Test
    void ensureInvalidIsbnThrowsExceptionWithAlphaCharacters() {
        String invalidIsbn = "978123A56789"; // ISBN com caracteres alfabéticos
        assertThrows(IllegalArgumentException.class, () -> new Book(invalidIsbn, validTitle, "Descrição", validGenre, authors, null));
    }

    // Teste de mutação para descrição com comprimento exato
    @Test
    void ensureDescriptionWithExactMaxLength() {
        String exactLengthDescription = "a".repeat(4096); // 4096 caracteres
        assertDoesNotThrow(() -> new Book(validIsbn, validTitle, exactLengthDescription, validGenre, authors, null));
    }


    ///FFFF


    // Teste de mutação para descrição com comprimento exato (limite de 4096 caracteres)
    @Test
    void ensureDescriptionWithExactMaxLengthDoesNotThrowException() {
        String maxDescription = "a".repeat(4096); // Exatamente 4096 caracteres
        assertDoesNotThrow(() -> new Book(validIsbn, validTitle, maxDescription, validGenre, authors, null));
    }

    // Teste de mutação para patch de ISBN nulo (não deve alterar o ISBN)
    @Test
    void ensureNullIsbnInPatchDoesNotChangeIsbn() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setIsbn(null);

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request));
        assertEquals(validIsbn, book.getIsbn());
    }

    // Teste de mutação para patch com género nulo (não deve alterar o género)
    @Test
    void ensureNullGenreInPatchDoesNotChangeGenre() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setGenreObj(null);

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request));
        assertEquals(validGenre, book.getGenre());
    }

    @Test
    void ensureNullAuthorsInPatchDoesNotChangeAuthors() {
        Book book = new Book(validIsbn, validTitle, "Descrição", validGenre, authors, null);
        UpdateBookRequest request = new UpdateBookRequest();
        request.setAuthorObjList(null);

        assertDoesNotThrow(() -> book.applyPatch(book.getVersion(), request));
        assertEquals(authors, book.getAuthors());
    }

    @Test
    void ensureExcessivelyLongTitleThrowsException() {
        String longTitle = "T".repeat(256); // Supondo que o limite de título seja 255 caracteres
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, longTitle, "Descrição", validGenre, authors, null));
    }

    @Test
    void ensureEmptyIsbnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Book("", validTitle, "Descrição", validGenre, authors, null));
    }


}