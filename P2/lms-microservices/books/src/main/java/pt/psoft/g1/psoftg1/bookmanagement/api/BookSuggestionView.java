package pt.psoft.g1.psoftg1.bookmanagement.api;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookSuggestionView {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String photoURI;
    private List<Long> authors;
    private String isbn;
    private Long suggestedByReaderId;
    private String status;
}
