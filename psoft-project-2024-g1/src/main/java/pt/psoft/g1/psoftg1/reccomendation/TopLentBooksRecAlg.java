package pt.psoft.g1.psoftg1.reccomendation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import java.util.Optional;

@Component("topLentBooksRecAlg")
public class TopLentBooksRecAlg implements RecommendationAlgorithm {

    @Value("${suggestionsLimitPerGenre}")
    private long maxRecommendationsPerGenre;

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final GenreRepository genreRepository;

    public TopLentBooksRecAlg(BookRepository bookRepository, ReaderRepository readerRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Book> getCustomRecommendations(String readerNumber) {
        ReaderDetails reader = readerRepository.findByReaderNumber(readerNumber)
            .orElseThrow(() -> new NotFoundException("Reader not found with the provided ID"));

        // Obter os géneros mais emprestados
        Pageable pageable = PageRequest.of(0, 5); // Top 5 géneros
        Page<GenreBookCountDTO> topGenres = genreRepository.findTop5GenreByBookCount(pageable);

        // Obter o género mais emprestado
        String mostLentGenre = topGenres.getContent().stream()
            .findFirst()
            .map(GenreBookCountDTO::getGenre)
            .orElseThrow(() -> new NotFoundException("No genres found"));

        // Buscar livros desse género
        List<Book> recommendations = bookRepository.findByGenre(mostLentGenre.toString());

        return recommendations.stream()
            .limit(maxRecommendationsPerGenre)
            .collect(Collectors.toList());
    }
}
