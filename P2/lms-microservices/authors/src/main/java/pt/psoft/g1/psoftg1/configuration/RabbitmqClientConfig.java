package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        // Shared exchange for all microservices
        return new DirectExchange("LMS");
    }

    @Bean
    public Queue authorCreatedQueue() {
        return new Queue("AUTHOR_CREATED", true);
    }

    @Bean
    public Queue authorUpdatedQueue() {
        return new Queue("AUTHOR_UPDATED", true);
    }

    @Bean
    public Queue authorDeletedQueue() {
        return new Queue("AUTHOR_DELETED", true);
    }

    @Bean
    public Binding authorCreatedBinding(DirectExchange direct, Queue authorCreatedQueue) {
        return BindingBuilder.bind(authorCreatedQueue).to(direct).with(AuthorEvents.AUTHOR_CREATED);
    }

    @Bean
    public Binding authorUpdatedBinding(DirectExchange direct, Queue authorUpdatedQueue) {
        return BindingBuilder.bind(authorUpdatedQueue).to(direct).with(AuthorEvents.AUTHOR_UPDATED);
    }

    @Bean
    public Binding authorDeletedBinding(DirectExchange direct, Queue authorDeletedQueue) {
        return BindingBuilder.bind(authorDeletedQueue).to(direct).with(AuthorEvents.AUTHOR_DELETED);
    }
}
