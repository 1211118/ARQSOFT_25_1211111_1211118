package pt.psoft.g1.psoftg1.readermanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ReaderServiceIntegrationTest {

   @Autowired
private AuthorRepository authorRepository;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private GenreRepository genreRepository;

    private CreateReaderRequest createReaderRequest;

    @Autowired
    private BookRepository bookRepository;

    private ReaderDetails readerDetails;

     @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private LendingRepository lendingRepository;

    @BeforeEach
    public void setUp() {
        // Criando gênero de teste
        Genre genre = new Genre("Ficção Científica");
        genreRepository.save(genre);

        // Criando uma requisição de leitor válida
        createReaderRequest = new CreateReaderRequest();
        createReaderRequest.setUsername("manuel@gmail.com");
        createReaderRequest.setPassword("Manuelino123!");
        createReaderRequest.setFullName("Manuel Sarapinto das Coives");
        createReaderRequest.setBirthDate("2000-01-01");
        createReaderRequest.setPhoneNumber("919191919");
        createReaderRequest.setGdpr(true);
        createReaderRequest.setMarketing(true);
        createReaderRequest.setThirdParty(false);
        createReaderRequest.setInterestList(List.of("Ficção Científica"));
    }

    @Test
    public void testCreateReaderSuccess() {
        // Criação de um leitor
        ReaderDetails createdReader = readerService.create(createReaderRequest, null);
        assertThat(createdReader).isNotNull();
        assertThat(createdReader.getReader().getUsername()).isEqualTo(createReaderRequest.getUsername());
        
    }

    @Test
    public void testCreateReaderConflictException() {
        // Criação de leitor com username duplicado para disparar uma ConflictException
        readerService.create(createReaderRequest, null);
        assertThrows(ConflictException.class, () -> readerService.create(createReaderRequest, null));
    }

    @Test
    public void testFindReaderByReaderNumber() {
        // Criação e busca de um leitor por número de leitor
        ReaderDetails createdReader = readerService.create(createReaderRequest, null);
        Optional<ReaderDetails> foundReader = readerService.findByReaderNumber(createdReader.getReaderNumber());
        assertThat(foundReader).isPresent();
        assertThat(foundReader.get().getReaderNumber()).isEqualTo(createdReader.getReaderNumber());
    }

    @Test
    public void testFindReaderByUsername() {
        // Criação e busca de um leitor por nome de usuário
        readerService.create(createReaderRequest, null);
        Optional<ReaderDetails> foundReader = readerService.findByUsername(createReaderRequest.getUsername());
        assertThat(foundReader).isPresent();
        assertThat(foundReader.get().getReader().getUsername()).isEqualTo(createReaderRequest.getUsername());
    }

    @Test
    public void testFindReaderByPhoneNumber() {
        // Criação e busca de um leitor por número de telefone
        readerService.create(createReaderRequest, null);
        List<ReaderDetails> foundReaders = readerService.findByPhoneNumber(createReaderRequest.getPhoneNumber());
        assertThat(foundReaders).isNotEmpty();
        assertThat(foundReaders.get(0).getPhoneNumber()).isEqualTo(createReaderRequest.getPhoneNumber());
    }

    @Test
    public void testUpdateReaderDetails() {
        // Criação e atualização dos detalhes do leitor
        ReaderDetails createdReader = readerService.create(createReaderRequest, null);

        UpdateReaderRequest updateRequest = new UpdateReaderRequest();
        updateRequest.setUsername("manuel_updated@gmail.com");
        updateRequest.setPhoneNumber("929292929");
        updateRequest.setInterestList(List.of("Ficção Científica"));

        ReaderDetails updatedReader = readerService.update(createdReader.getReader().getId(), updateRequest, createdReader.getVersion(), null);

        assertThat(updatedReader.getReader().getUsername()).isEqualTo("manuel_updated@gmail.com");
        assertThat(updatedReader.getPhoneNumber()).isEqualTo("929292929");
    }

    @Test
    public void testSearchReaders() {
        // Criação e busca de leitores usando critérios personalizados
        readerService.create(createReaderRequest, null);

        SearchReadersQuery query = new SearchReadersQuery();
        query.setName("Manuel");
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

        List<ReaderDetails> searchResults = readerService.searchReaders(page, query);
        assertThat(searchResults).isNotEmpty();
    }

    @Test
    public void testSearchReadersNotFound() {
        // Teste para verificar a exceção NotFoundException ao não encontrar resultados
        SearchReadersQuery query = new SearchReadersQuery();
        query.setName("Nonexistent Name");
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

        assertThrows(NotFoundException.class, () -> readerService.searchReaders(page, query));
    }

}
