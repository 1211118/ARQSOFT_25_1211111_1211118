package pt.psoft.g1.psoftg1.authormanagement.services;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorServiceImplIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;
    
    @MockBean
    private BookRepository bookRepository;
    
    @MockBean
    private PhotoRepository photoRepository;

    private Author testAuthor;
    private List<Author> authorList;
    
    @BeforeEach
    public void setUp() {
        testAuthor = new Author("Alex", "O Alex escreveu livros", null);
        authorList = new ArrayList<>();
        authorList.add(testAuthor);

        // Mock responses for author by name
        when(authorRepository.searchByNameName(testAuthor.getName())).thenReturn(authorList);
        // Mock response for author by ID
        when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(testAuthor));
        // Mock response for book by author number
        when(bookRepository.findBooksByAuthorNumber(1L)).thenReturn(new ArrayList<>());
    }

    @Test
    public void whenValidId_thenAuthorShouldBeFound() {
        Long id = 1L;
        Optional<Author> found = authorService.findByAuthorNumber(id);
        assertTrue(found.isPresent());
    }

    @Test
    public void whenInvalidId_thenAuthorShouldNotBeFound() {
        Long id = 2L;
        Optional<Author> found = authorService.findByAuthorNumber(id);
        assertTrue(found.isEmpty());
    }

    @Test
    public void whenPartialUpdateValid_thenAuthorIsUpdated() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest();
        updateRequest.setName("New Alex");
        updateRequest.setBio("Updated Bio");

        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(testAuthor);

        Author updatedAuthor = authorService.partialUpdate(1L, updateRequest, testAuthor.getVersion());
        
        assertThat(updatedAuthor.getName()).isEqualTo("New Alex");
        assertThat(updatedAuthor.getBio()).isEqualTo("Updated Bio");
        verify(authorRepository, times(1)).save(Mockito.any(Author.class));
    }

    @Test
    public void whenFindTopAuthorByLendings_thenReturnList() {
        List<AuthorLendingView> topAuthors = new ArrayList<>();
        topAuthors.add(new AuthorLendingView("Alex", 10L));
        
        when(authorRepository.findTopAuthorByLendings(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(topAuthors));

        List<AuthorLendingView> foundAuthors = authorService.findTopAuthorByLendings();
        assertEquals(1, foundAuthors.size());
        assertEquals("Alex", foundAuthors.get(0).getAuthorName());
    }

    @Test
    public void whenFindBooksByAuthorNumber_thenReturnBooks() {
        Long authorId = 1L;
        List<Book> books = authorService.findBooksByAuthorNumber(authorId);
        assertNotNull(books);
        verify(bookRepository, times(1)).findBooksByAuthorNumber(authorId);
    }

    @Test
    public void whenFindCoAuthorsByAuthorNumber_thenReturnCoAuthors() {
        List<Author> coAuthors = new ArrayList<>();
        coAuthors.add(new Author("CoAuthor1", "Bio of CoAuthor1", null));
        
        when(authorRepository.findCoAuthorsByAuthorNumber(1L)).thenReturn(coAuthors);

        List<Author> foundCoAuthors = authorService.findCoAuthorsByAuthorNumber(1L);
        assertEquals(1, foundCoAuthors.size());
        assertEquals("CoAuthor1", foundCoAuthors.get(0).getName());
    }

    @Test
public void whenCreateAuthorWithValidData_thenAuthorIsCreated() {
    CreateAuthorRequest request = new CreateAuthorRequest("Bob", "Bio of Bob", null, null); 
    Author newAuthor = new Author(request.getName(), request.getBio(), null);

    when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

    Author createdAuthor = authorService.create(request);

    assertNotNull(createdAuthor);
    assertEquals("Bob", createdAuthor.getName());
    verify(authorRepository, times(1)).save(any(Author.class));
}

    // Teste de criação com nome nulo (deve falhar)
    @Test
    public void whenCreateAuthorWithNullName_thenThrowException() {
        CreateAuthorRequest request = new CreateAuthorRequest(null, "Bio of Bob", null, null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            authorService.create(request);
        });
    }

    // Teste de atualização parcial com versão incorreta
    @Test
    public void whenPartialUpdateWithIncorrectVersion_thenThrowConflictException() {
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setName("New Alex");

        // Configurando versão desatualizada
        long outdatedVersion = testAuthor.getVersion() - 1;

        when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(testAuthor));

        assertThrows(StaleObjectStateException.class, () -> {
            authorService.partialUpdate(1L, request, outdatedVersion);
        });
    }

    @Test
public void whenConcurrentModification_thenThrowStaleObjectStateException() {
    UpdateAuthorRequest request = new UpdateAuthorRequest();
    request.setBio("Updated bio");

    long initialVersion = testAuthor.getVersion();

    // Configura o mock para retornar o autor com a versão inicial
    when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(testAuthor));

    // Simula uma atualização concorrente ao alterar a versão do autor antes da chamada save
    when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> {
        // Atualiza a versão do autor para simular uma modificação concorrente
        throw new ConflictException("Object was already modified by another user");
    });

    assertThrows(ConflictException.class, () -> {
        authorService.partialUpdate(1L, request, initialVersion);
    });
}


    // Teste para getTopAuthorByLendings quando não há autores
    @Test
    public void whenNoAuthors_thenFindTopAuthorByLendingsShouldReturnEmptyList() {
        when(authorRepository.findTopAuthorByLendings(PageRequest.of(0, 5))).thenReturn(Page.empty());
        
        List<AuthorLendingView> topAuthors = authorService.findTopAuthorByLendings();
        
        assertTrue(topAuthors.isEmpty());
    }

    // Teste de busca por coautores quando nenhum é encontrado
    @Test
    public void whenNoCoAuthors_thenFindCoAuthorsShouldReturnEmptyList() {
        when(authorRepository.findCoAuthorsByAuthorNumber(1L)).thenReturn(new ArrayList<>());

        List<Author> coAuthors = authorService.findCoAuthorsByAuthorNumber(1L);

        assertTrue(coAuthors.isEmpty());
    }

    // Teste para caso de autor não encontrado na exclusão de foto
    @Test
    public void whenAuthorNotFound_thenRemovePhotoShouldThrowNotFoundException() {
        when(authorRepository.findByAuthorNumber(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            authorService.removeAuthorPhoto(2L, 1L);
        });
    }

    // Teste de atualização parcial com campos nulos (não devem alterar os valores)
    @Test
    public void whenPartialUpdateWithNullFields_thenExistingValuesShouldRemain() {
        UpdateAuthorRequest request = new UpdateAuthorRequest(); // Todos os campos nulos

        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor);

        Author updatedAuthor = authorService.partialUpdate(1L, request, testAuthor.getVersion());

        assertEquals("Alex", updatedAuthor.getName());
        assertEquals("O Alex escreveu livros", updatedAuthor.getBio());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    

    
}
