package pt.psoft.g1.psoftg1.authormanagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Based on https://www.baeldung.com/spring-boot-testing
 * <p>Adaptations to Junit 5 with ChatGPT
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void whenFindByName_thenReturnAuthor() {
        // given
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        entityManager.persist(alex);
        entityManager.flush();

        // when
        List<Author> list = authorRepository.searchByNameName(alex.getName());

        // then
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName())
                .isEqualTo(alex.getName());
    }

    @Test
    public void whenFindByAuthorNumber_thenReturnAuthor() {
        Author author = new Author("Maria", "A Maria escreveu mais livros", null);
        entityManager.persist(author);
        entityManager.flush();

        Optional<Author> found = authorRepository.findByAuthorNumber(author.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(author.getName());
    }

    @Test
    public void whenSearchByNameStartsWith_thenReturnMatchingAuthors() {
        Author author1 = new Author("Ana", "Uma autora chamada Ana", null);
        Author author2 = new Author("Antonio", "Outro autor chamado Antonio", null);
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.flush();

        List<Author> list = authorRepository.searchByNameNameStartsWith("An");

        assertThat(list).hasSize(2);
        assertThat(list).extracting(Author::getName).containsExactlyInAnyOrder("Ana", "Antonio");
    }

    @Test
    public void whenSaveAuthor_thenReturnSavedAuthor() {
        Author newAuthor = new Author("Carlos", "O Carlos é um escritor novo", null);
        
        Author savedAuthor = authorRepository.save(newAuthor);

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getId()).isNotNull();
        assertThat(savedAuthor.getName()).isEqualTo("Carlos");
    }

    @Test
    public void whenFindAll_thenReturnAllAuthors() {
        Author author1 = new Author("Paula", "Escritora de romance", null);
        Author author2 = new Author("Pedro", "Autor de ficção científica", null);
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.flush();

        Iterable<Author> authors = authorRepository.findAll();

        assertThat(authors).hasSize(2);
    }

    @Test
    public void whenDeleteAuthor_thenAuthorIsRemoved() {
        Author authorToDelete = new Author("Lucas", "Autor para deletar", null);
        entityManager.persist(authorToDelete);
        entityManager.flush();

        authorRepository.delete(authorToDelete);
        Optional<Author> deletedAuthor = authorRepository.findByAuthorNumber(authorToDelete.getId());

        assertThat(deletedAuthor).isEmpty();
    }


    @Test
    public void whenFindCoAuthorsByAuthorNumber_thenReturnCoAuthors() {
        Genre genre = new Genre("Romance");
        entityManager.persist(genre);

        Author mainAuthor = new Author("Tiago", "Escreveu com coautores", null);
        Author coAuthor1 = new Author("Joana", "Coautora de Tiago", null);
        Author coAuthor2 = new Author("Carlos", "Outro coautor", null);
        Author unrelatedAuthor = new Author("Pedro", "Autor sem relação", null);

        entityManager.persist(mainAuthor);
        entityManager.persist(coAuthor1);
        entityManager.persist(coAuthor2);
        entityManager.persist(unrelatedAuthor);

        Book sharedBook1 = new Book("9789896375225", "Livro com Tiago e Joana", "Descrição", genre, List.of(mainAuthor, coAuthor1), null);
        Book sharedBook2 = new Book("9789896378905", "Livro com Tiago e Carlos", "Descrição", genre, List.of(mainAuthor, coAuthor2), null);
        entityManager.persist(sharedBook1);
        entityManager.persist(sharedBook2);

        entityManager.flush();

        List<Author> coAuthors = authorRepository.findCoAuthorsByAuthorNumber(mainAuthor.getId());

        assertThat(coAuthors).containsExactlyInAnyOrder(coAuthor1, coAuthor2);
        assertThat(coAuthors).doesNotContain(mainAuthor, unrelatedAuthor);
    }

 

}
