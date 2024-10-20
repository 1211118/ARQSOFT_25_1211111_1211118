package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

     @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        String queryStr = "SELECT a FROM Author a WHERE a.authorNumber = :authorNumber";
        TypedQuery<Author> query = entityManager.createQuery(queryStr, Author.class);
        query.setParameter("authorNumber", authorNumber);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        String queryStr = "SELECT a FROM Author a WHERE a.name.name LIKE :namePrefix";
        TypedQuery<Author> query = entityManager.createQuery(queryStr, Author.class); // Use TypedQuery aqui
        query.setParameter("namePrefix", name + "%");
        return query.getResultList();
    }

    @Override
    public List<Author> searchByNameName(String name) {
        String queryStr = "SELECT a FROM Author a WHERE a.name.name = :name";
        TypedQuery<Author> query = entityManager.createQuery(queryStr, Author.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            entityManager.persist(author);
        } else {
            entityManager.merge(author);
        }
        return author;
    }

    @Override
    public Iterable<Author> findAll() {
        String queryStr = "SELECT a FROM Author a";
        TypedQuery<Author> query = entityManager.createQuery(queryStr, Author.class);
        return query.getResultList();
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
        String queryStr = "SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.pk)) " +
                          "FROM Book b " +
                          "JOIN b.authors a " +
                          "JOIN Lending l ON l.book.pk = b.pk " +
                          "GROUP BY a.name " +
                          "ORDER BY COUNT(l) DESC";
        
        TypedQuery<AuthorLendingView> query = entityManager.createQuery(queryStr, AuthorLendingView.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        List<AuthorLendingView> resultList = query.getResultList();
        long total = resultList.size();
        
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public void delete(Author author) {
        if (entityManager.contains(author)) {
            entityManager.remove(author);
        } else {
            Author attachedAuthor = entityManager.merge(author);
            entityManager.remove(attachedAuthor);
        }
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        String queryStr = "SELECT DISTINCT coAuthor FROM Book b " +
                          "JOIN b.authors coAuthor " +
                          "WHERE b IN (SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber) " +
                          "AND coAuthor.authorNumber <> :authorNumber";
        
        TypedQuery<Author> query = entityManager.createQuery(queryStr, Author.class);
        query.setParameter("authorNumber", authorNumber);
        return query.getResultList();
    }

    


    
}
