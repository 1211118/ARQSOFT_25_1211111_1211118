/*package pt.psoft.g1.psoftg1.bookSuggestionManagement.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.channel.PollableAmqpChannel;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.messaging.PollableChannel;

@TestConfiguration
public class TestConfig {

    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange("LMS");
    }

    @Bean
    public Queue testBookSuggestionsQueue() {
        return new Queue("LMS.book.suggestions", true);
    }

    @Bean
    public Binding bookSuggestionsBinding(Queue testBookSuggestionsQueue, DirectExchange testDirectExchange) {
        return BindingBuilder.bind(testBookSuggestionsQueue)
                .to(testDirectExchange)
                .with("LMS.book.suggestions");
    }

    @Bean(name = "LMS.book.suggestions")
    public PollableChannel bookSuggestionsChannel(ConnectionFactory connectionFactory) {
        return Amqp.pollableChannel(connectionFactory)
                .queueName("LMS.book.suggestions")
                .get(); // Canal vinculado diretamente à fila no RabbitMQ
    }
}
*/