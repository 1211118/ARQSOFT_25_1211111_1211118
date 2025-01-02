package pt.psoft.g1.psoftg1.authormanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQPMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.publishers.AuthorEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;

@Service
@RequiredArgsConstructor
public class AuthorEventsRabbitmqPublisherImpl implements AuthorEventsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final AuthorViewAMQPMapper mapper;

    @Override
    public void sendAuthorCreated(Author author) {
        publishEvent(author, AuthorEvents.AUTHOR_CREATED);
    }

    @Override
    public void sendAuthorUpdated(Author author) {
        publishEvent(author, AuthorEvents.AUTHOR_UPDATED);
    }

    @Override
    public void sendAuthorDeleted(Author author) {
        publishEvent(author, AuthorEvents.AUTHOR_DELETED);
    }

    private void publishEvent(Author author, String routingKey) {
        try {
            AuthorViewAMQP view = mapper.toAuthorViewAMQP(author);
            String message = new ObjectMapper().writeValueAsString(view);
            rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, message);
            System.out.println(" [x] Sent event to RabbitMQ: " + routingKey + " -> " + message);
        } catch (Exception e) {
            System.err.println(" [!] Failed to send RabbitMQ event: " + e.getMessage());
        }
    }
}
