package pt.psoft.g1.psoftg1.authormanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorEventRabbitmqReceiver {

    @RabbitListener(queues = "AUTHOR_CREATED")
    public void handleAuthorCreated(String message) {
        try {
            AuthorViewAMQP author = new ObjectMapper().readValue(message, AuthorViewAMQP.class);
            System.out.println(" [x] Received AUTHOR_CREATED event: " + author);
        } catch (Exception e) {
            System.err.println(" [!] Error processing AUTHOR_CREATED event: " + e.getMessage());
        }
    }
}
