package pt.psoft.g1.psoftg1.authormanagement.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.idmanagement.IdGenerator;

@Component
public class AuthorFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${idGenerator}")
    private String selectedIdGenerator;

    /**
     * Cria uma nova instância de Author com o ID gerado.
     */
    public Author createAuthor(String name, String bio, String photoURI) {
        IdGenerator idGenerator = applicationContext.getBean(selectedIdGenerator, IdGenerator.class);
        String authorId = idGenerator.generateId();  // Gera o authorId
        return new Author(name, bio, photoURI, authorId);
    }
}
