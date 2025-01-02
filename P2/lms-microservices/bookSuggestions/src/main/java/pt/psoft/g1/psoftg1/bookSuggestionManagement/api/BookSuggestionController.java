package pt.psoft.g1.psoftg1.bookSuggestionManagement.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.services.BookSuggestionService;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.services.CreateBookSuggestionRequest;
import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class BookSuggestionController {

    private final BookSuggestionService bookSuggestionService;

    @PostMapping(value = "/suggest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookSuggestionView> suggestBook(
            @RequestParam("title") String title,
            @RequestParam("genre") String genre,
            @RequestParam("authors") List<Long> authors,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam("readerId") Long readerId) {
        
        CreateBookSuggestionRequest request = CreateBookSuggestionRequest.builder()
                .title(title)
                .description(description)
                .genre(genre)
                .authors(authors)
                .photo(photo)
                .readerId(readerId)
                .build();
        
        return ResponseEntity.ok(bookSuggestionService.suggestBook(request));
    }
}
