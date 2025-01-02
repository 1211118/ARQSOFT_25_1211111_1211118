package pt.psoft.g1.psoftg1.genremanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.publishers.GenreEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Service
@RequiredArgsConstructor
public class GenreEventsRabbitmqPublisherImpl implements GenreEventsPublisher {

    private final RabbitTemplate template;
    private final DirectExchange direct;

    @Override
    public void sendGenreCreated(Genre genre) {
        sendGenreEvent(genre, GenreEvents.GENRE_CREATED);
    }

    @Override
    public void sendGenreUpdated(Genre genre, Long currentVersion) {
        sendGenreEvent(genre, GenreEvents.GENRE_UPDATED);
    }

    @Override
    public void sendGenreDeleted(Genre genre, Long currentVersion) {
        sendGenreEvent(genre, GenreEvents.GENRE_DELETED);
    }

    private void sendGenreEvent(Genre genre, String eventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(genre);
            this.template.convertAndSend(direct.getName(), eventType, jsonString);
            System.out.println(" [x] Sent '" + jsonString + "'");
        } catch (Exception ex) {
            System.out.println(" [x] Exception sending genre event: '" + ex.getMessage() + "'");
        }
    }
}
