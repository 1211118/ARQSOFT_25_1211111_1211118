package pt.psoft.g1.psoftg1.genremanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@Component
@RequiredArgsConstructor
public class GenreEventRabbitmqReceiver {

    private final GenreService genreService;

    @RabbitListener(queues = "GENRE_CREATED")
public void receiveGenreCreated(String message) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        GenreView genreViewAMQP = objectMapper.readValue(message, GenreView.class);

        // Verificar se o gênero já existe no banco de dados
        Optional<Genre> existingGenre = genreService.findByString(genreViewAMQP.getGenre());
        if (existingGenre.isPresent()) {
            System.out.println(" [x] Genre already exists: " + genreViewAMQP.getGenre());
            return; // Não tentar inserir novamente
        }

        // Criar e salvar o novo gênero
        Genre genre = new Genre(genreViewAMQP.getGenre());
        genreService.save(genre);
        System.out.println(" [x] Genre created successfully: " + genreViewAMQP.getGenre());

    } catch (Exception ex) {
        System.err.println(" [!] Exception receiving GENRE_CREATED event: '" + ex.getMessage() + "'");
    }
}

}
