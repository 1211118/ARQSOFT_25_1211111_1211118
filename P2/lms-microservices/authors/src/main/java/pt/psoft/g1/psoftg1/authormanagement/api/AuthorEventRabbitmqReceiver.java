package pt.psoft.g1.psoftg1.authormanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorEventRabbitmqReceiver {

    private final AuthorService authorService;

    @RabbitListener(queues = "AUTHOR_CREATED")
    public void handleAuthorCreated(String message) {
        try {
            AuthorViewAMQP author = new ObjectMapper().readValue(message, AuthorViewAMQP.class);
            System.out.println(" [x] Received AUTHOR_CREATED event: " + author);
            CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest(author.getName(),author.getBio(),null,null);
            List<Author> existingAuthor = authorService.findByName(createAuthorRequest.getName());
            if(existingAuthor.size() > 0){
                System.out.println(" [x] Author already exists: " + createAuthorRequest.getName());
                return;
            }
            authorService.create(createAuthorRequest);

        } catch (Exception e) {
            System.err.println(" [!] Error processing AUTHOR_CREATED event: " + e.getMessage());
        }
    }
}
