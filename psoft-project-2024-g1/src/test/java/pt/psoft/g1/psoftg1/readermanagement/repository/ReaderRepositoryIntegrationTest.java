package pt.psoft.g1.psoftg1.readermanagement.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ReaderRepositoryIntegrationTest {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private UserRepository userRepository;

    private ReaderDetails readerDetails1;
    private ReaderDetails readerDetails2;
    private ReaderDetails readerDetails3;

    @BeforeEach
    public void setUp() {
        // Criando leitores com diferentes dados
        Reader reader1 = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
        userRepository.save(reader1);
        readerDetails1 = new ReaderDetails(1, reader1, "2000-01-01", "919191919", true, true, true, null, List.of());
        readerRepository.save(readerDetails1);

        Reader reader2 = Reader.newReader("joao@gmail.com", "JoaoRatao123!", "João Ratao");
        userRepository.save(reader2);
        readerDetails2 = new ReaderDetails(2, reader2, "1995-06-02", "929292929", true, false, false, null, List.of());
        readerRepository.save(readerDetails2);

        Reader reader3 = Reader.newReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins");
        userRepository.save(reader3);
        readerDetails3 = new ReaderDetails(3, reader3, "1990-03-10", "939393939", true, true, false, null, List.of());
        readerRepository.save(readerDetails3);
    }

    @Test
    public void testSearchReaderDetailsByName() {
        // Busca pelo nome "Manuel"
        SearchReadersQuery query = new SearchReadersQuery();
        query.setName("Manuel");
        Page page = new Page(1, 10);

        List<ReaderDetails> results = readerRepository.searchReaderDetails(page, query);
        assertThat(results).contains(readerDetails1).doesNotContain(readerDetails2, readerDetails3);
    }

    @Test
    public void testSearchReaderDetailsByEmail() {
        // Busca pelo email "joao@gmail.com"
        SearchReadersQuery query = new SearchReadersQuery();
        query.setEmail("joao@gmail.com");
        Page page = new Page(1, 10);

        List<ReaderDetails> results = readerRepository.searchReaderDetails(page, query);
        assertThat(results).contains(readerDetails2).doesNotContain(readerDetails1, readerDetails3);
    }

    @Test
    public void testSearchReaderDetailsByPhoneNumber() {
        // Busca pelo telefone "939393939"
        SearchReadersQuery query = new SearchReadersQuery();
        query.setPhoneNumber("939393939");
        Page page = new Page(1, 10);

        List<ReaderDetails> results = readerRepository.searchReaderDetails(page, query);
        assertThat(results).contains(readerDetails3).doesNotContain(readerDetails1, readerDetails2);
    }

    @Test
    public void testSearchReaderDetailsByMultipleCriteria() {
        // Busca por nome "Catarina" e email "catarina@gmail.com"
        SearchReadersQuery query = new SearchReadersQuery();
        query.setName("Catarina");
        query.setEmail("catarina@gmail.com");
        Page page = new Page(1, 10);

        List<ReaderDetails> results = readerRepository.searchReaderDetails(page, query);
        assertThat(results).contains(readerDetails3).doesNotContain(readerDetails1, readerDetails2);
    }

    @Test
    public void testSearchReaderDetailsNoResults() {
        // Busca por nome que não existe
        SearchReadersQuery query = new SearchReadersQuery();
        query.setName("Nonexistent");
        Page page = new Page(1, 10);

        List<ReaderDetails> results = readerRepository.searchReaderDetails(page, query);
        assertThat(results).isEmpty();
    }
}
