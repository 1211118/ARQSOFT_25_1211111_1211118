package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS.genres");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Genre_Created")
        public Queue autoDeleteQueue_Genre_Created() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Genre_Updated() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Genre_Deleted() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Genre_Created") Queue autoDeleteQueue_Genre_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Created)
                    .to(direct)
                    .with(GenreEvents.GENRE_CREATED);
        }

        @Bean
        public Binding binding2(DirectExchange direct,
                                Queue autoDeleteQueue_Genre_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Updated)
                    .to(direct)
                    .with(GenreEvents.GENRE_UPDATED);
        }

        @Bean
        public Binding binding3(DirectExchange direct,
                                Queue autoDeleteQueue_Genre_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Deleted)
                    .to(direct)
                    .with(GenreEvents.GENRE_DELETED);
        }
    }
}
