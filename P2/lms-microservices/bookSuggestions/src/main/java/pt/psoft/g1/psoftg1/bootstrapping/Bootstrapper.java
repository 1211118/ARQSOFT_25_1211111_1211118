package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.model.BookSuggestion;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.repositories.BookSuggestionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("bootstrap")
@RequiredArgsConstructor
public class Bootstrapper implements CommandLineRunner {

    private final BookSuggestionRepository bookSuggestionRepository;

    @Override
    public void run(String... args) {
        // Exemplo de dados iniciais
        BookSuggestion suggestion = BookSuggestion.builder()
                .title("Example Book")
                .description("Example description")
                .genre("Fiction")
                .photoURI("example/photo/path")
                .authors(List.of(1L)) // Adicionando um autor com ID 1
                .suggestedByReaderId(1L)
                .status("PENDING")
                .build();

        bookSuggestionRepository.save(suggestion);
    }
}
