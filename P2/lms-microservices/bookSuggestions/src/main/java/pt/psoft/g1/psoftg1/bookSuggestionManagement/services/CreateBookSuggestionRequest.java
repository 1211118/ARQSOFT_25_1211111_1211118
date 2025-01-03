package pt.psoft.g1.psoftg1.bookSuggestionManagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a book suggestion")
public class CreateBookSuggestionRequest {
    
    @Schema(description = "Title of the suggested book", 
            example = "The Lord of the Rings",
            required = true)
    private String title;

    @Schema(description = "Description of the suggested book", 
            example = "An epic high-fantasy novel")
    private String description;

    @Schema(description = "Genre of the book", 
            example = "Fantasy",
            required = true)
    private String genre;

    @Schema(description = "IDs of the authors", 
            example = "[1, 2]",
            required = true)
    private List<Long> authors;

    @Schema(description = "Isbn number of the book", 
            example = "0-1128-5240-8",
            required = true)
    private String isbn;

    @Schema(description = "ID of the reader making the suggestion", 
            example = "1")
    private Long readerId;

    @Schema(description = "Photo of the book (optional)")
    private MultipartFile photo;
}
