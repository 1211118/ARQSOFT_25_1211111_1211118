package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookSuggestionEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.BookSuggestionEvents;


@Profile("!test")
@Configuration
public  class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Book_Created")
        public Queue autoDeleteQueue_Book_Created() {

            System.out.println("autoDeleteQueue_Book_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Book_Updated() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Book_Deleted() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_BookSuggestion_Created")
        public Queue autoDeleteQueue_BookSuggestion_Created() {
            System.out.println("autoDeleteQueue_BookSuggestion_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
                    .to(direct)
                    .with(BookEvents.BOOK_CREATED);
        }

        @Bean
        public Binding binding2(DirectExchange direct,
                                Queue autoDeleteQueue_Book_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Updated)
                    .to(direct)
                    .with(BookEvents.BOOK_UPDATED);
        }

        @Bean
        public Binding binding3(DirectExchange direct,
                                Queue autoDeleteQueue_Book_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Deleted)
                    .to(direct)
                    .with(BookEvents.BOOK_DELETED);
        }

        @Bean
        public Binding bindingBookSuggestion(DirectExchange direct,
                                   Queue autoDeleteQueue_BookSuggestion_Created) {
            return BindingBuilder.bind(autoDeleteQueue_BookSuggestion_Created)
                    .to(direct)
                    .with(BookSuggestionEvents.BOOK_SUGGESTION_CREATED);
        }

        @Bean
        public BookEventRabbitmqReceiver receiver(BookService bookService, @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return new BookEventRabbitmqReceiver(bookService);
        }

        @Bean
        public BookSuggestionEventRabbitmqReceiver bookSuggestionReceiver(
            @Qualifier("autoDeleteQueue_BookSuggestion_Created") Queue autoDeleteQueue_BookSuggestion_Created) {
        return new BookSuggestionEventRabbitmqReceiver();
        }

        @Bean
        public Queue authorCreatedQueue() {
            return new Queue("AUTHOR_CREATED", true);
        }

        @Bean
        public Queue genreCreatedQueue() {
            return new Queue("GENRE_CREATED", true);
        }
    }
}
