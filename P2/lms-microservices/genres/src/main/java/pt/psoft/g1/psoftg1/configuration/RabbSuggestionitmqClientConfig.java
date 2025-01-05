package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Profile("!test")
@Configuration
public class RabbSuggestionitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS");
    }

    @Bean
    public Queue genreCreatedQueue() {
        return new Queue("GENRE_CREATED", true);
    }

    @Bean
    public Binding genreCreatedBinding(DirectExchange direct, Queue genreCreatedQueue) {
        return BindingBuilder.bind(genreCreatedQueue).to(direct).with(GenreEvents.GENRE_CREATED);
    }


}
