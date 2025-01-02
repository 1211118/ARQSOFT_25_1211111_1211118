package pt.psoft.g1.psoftg1.genremanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@Component
@RequiredArgsConstructor
public class GenreEventRabbitmqReceiver {

    private final GenreService genreService;
    

    @RabbitListener(queues = "#{autoDeleteQueue_Genre_Created.name}")
    public void receiveGenreCreated(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Genre genre = objectMapper.readValue(message, Genre.class);
            genreService.save(genre);
            System.out.println(" [x] Received Genre Created: " + genre);
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving genre created event: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Genre_Updated.name}")
    public void receiveGenreUpdated(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Genre genre = objectMapper.readValue(message, Genre.class);
            //genreService.update(genre, genre.getVersion());
            System.out.println(" [x] Received Genre Updated: " + genre);
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving genre updated event: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Genre_Deleted.name}")
    public void receiveGenreDeleted(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Genre genre = objectMapper.readValue(message, Genre.class);
            //genreService.delete(genre);
            System.out.println(" [x] Received Genre Deleted: " + genre);
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving genre deleted event: '" + ex.getMessage() + "'");
        }
    }
}
