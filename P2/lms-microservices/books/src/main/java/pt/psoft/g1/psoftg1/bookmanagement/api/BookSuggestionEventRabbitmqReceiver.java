package pt.psoft.g1.psoftg1.bookmanagement.api;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BookSuggestionEventRabbitmqReceiver {

    @RabbitListener(queues = "#{autoDeleteQueue_BookSuggestion_Created.name}")
    public void receiveBookSuggestionCreated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookSuggestionView bookSuggestionView = objectMapper.readValue(jsonReceived, BookSuggestionView.class);

            System.out.println(" [x] Received Book Suggestion Created: " + bookSuggestionView);
            // Here you can implement logic to notify librarians 
            // about the new book suggestion
            
        } catch(Exception ex) {
            System.out.println(" [x] Exception receiving book suggestion created event: '" + ex.getMessage() + "'");
        }
    }

}
