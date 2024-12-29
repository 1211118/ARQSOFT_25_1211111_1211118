package pt.psoft.g1.psoftg1.reccomendation;

import java.util.List;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

public interface RecommendationAlgorithm {
    List<Book> getCustomRecommendations(String readerNumber);
    
}
