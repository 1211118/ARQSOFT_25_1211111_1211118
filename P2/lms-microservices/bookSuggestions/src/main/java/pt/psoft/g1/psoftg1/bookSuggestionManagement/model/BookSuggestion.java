package pt.psoft.g1.psoftg1.bookSuggestionManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.model.Isbn;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(nullable = false)
    private String genre;
    
    private String photoURI;
    
    @ElementCollection
    @CollectionTable(name = "book_suggestion_authors",
            joinColumns = @JoinColumn(name = "book_suggestion_id"))
    @Column(name = "author_id", nullable = false)
    private List<Long> authors;

    @Column(nullable = false)
    Isbn isbn;
    
    private Long suggestedByReaderId;
        
    private String status;  // PENDING, APPROVED, REJECTED

    @Transient  // Não será persistido no banco de dados
    private byte[] photoData;
}
