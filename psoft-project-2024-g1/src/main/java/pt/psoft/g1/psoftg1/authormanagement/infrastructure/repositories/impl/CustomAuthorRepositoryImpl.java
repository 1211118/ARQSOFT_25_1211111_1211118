package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


public class CustomAuthorRepositoryImpl implements AuthorRepository {

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        // Exemplo de implementação simulada
        System.out.println("Usando CustomAuthorRepositoryImpl - Outra Base de Dados");
        return Optional.of(new Author("Simulated Author", "Simulated Last Name", null));
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        // Implementação simulada
        System.out.println("Buscando autores que começam com: " + name);
        return List.of(new Author("Simulated Name", "Simulated Last Name", name));
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
        // Implementação simulada
        System.out.println("Buscando autores com mais empréstimos");
        return Page.empty();
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        // Implementação simulada
        System.out.println("Buscando coautores na base simulada");
        return List.of();
    }

    @Override
    public List<Author> searchByNameName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByNameName'");
    }

    @Override
    public Author save(Author author) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Iterable<Author> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void delete(Author author) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    
}
