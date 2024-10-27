package pt.psoft.g1.psoftg1.reccomendation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

@Component("ageGroupRecAlg")
@PropertySource({"classpath:config/library.properties"})
public class AgeGroupRecAlg implements RecommendationAlgorithm {

    @Value("${childReaderAge}")
    private int childReaderAge;

    @Value("${adultReaderAge}")
    private int adultReaderAge;

    @Value("${suggestionsLimitPerGenre}")
    private long maxRecommendationsPerGenre;

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final TopLentBooksRecAlg topLentBooksRecAlg;

    @Autowired
    public AgeGroupRecAlg(BookRepository bookRepository, ReaderRepository readerRepository, TopLentBooksRecAlg topLentBooksRecAlg) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.topLentBooksRecAlg = topLentBooksRecAlg;
    }

    @Override
    public List<Book> getCustomRecommendations(String readerNumber) {

        ReaderDetails reader = readerRepository.findByReaderNumber(readerNumber)
             .orElseThrow(() -> new NotFoundException("Reader not found with the provided ID"));

        if (reader.getInterestList().isEmpty()) {
            throw new NotFoundException("Reader has no interests defined");
        }

        List<Book> recommendations = new ArrayList<>();
        int age = reader.getBirthDate().getAge();

        if (age < childReaderAge) {
            recommendations = getBooksByGenre("Infantil");
            System.out.println(recommendations);
        } else if (age < adultReaderAge) {
            recommendations = getBooksByGenre("Fantasia");
        } else {
            recommendations = topLentBooksRecAlg.getCustomRecommendations(readerNumber);
        }

        return recommendations.stream()
            .limit(maxRecommendationsPerGenre)
            .collect(Collectors.toList());
    }

    private List<Book> getBooksByGenre(String genreName) {
        return bookRepository.findByGenre(genreName);
    }
}
