package pt.psoft.g1.psoftg1.bookSuggestionManagement.infrastructure.publishers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.api.BookSuggestionView;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.publishers.BookSuggestionPublisher;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;

@Component
@RequiredArgsConstructor
public class BookSuggestionRabbitmqPublisherImpl implements BookSuggestionPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishBookSuggestion(BookSuggestionView bookSuggestionView) {
        rabbitTemplate.convertAndSend(RabbitmqClientConfig.BOOK_SUGGESTION_QUEUE, bookSuggestionView);
    }
}