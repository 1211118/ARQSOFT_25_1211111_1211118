package pt.psoft.g1.psoftg1.bookSuggestionManagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.api.BookSuggestionView;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.model.BookSuggestion;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.repositories.BookSuggestionRepository;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.services.BookSuggestionService;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.publishers.BookSuggestionPublisher;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.model.Isbn;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookSuggestionServiceImpl implements BookSuggestionService {

    private final BookSuggestionRepository bookSuggestionRepository;
    private final BookSuggestionPublisher bookSuggestionPublisher;
    private final FileStorageService fileStorageService;

    @Override
    public BookSuggestionView suggestBook(CreateBookSuggestionRequest request) {
        String photoURI = null;
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            photoURI = fileStorageService.storeFile(request.getReaderId().toString(), request.getPhoto());
        }

        BookSuggestion bookSuggestion = BookSuggestion.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .authors(request.getAuthors())
                .isbn(new Isbn(request.getIsbn()))
                .photoURI(photoURI)
                .suggestedByReaderId(request.getReaderId())
                .status("PENDING")
                .build();

        BookSuggestion saved = bookSuggestionRepository.save(bookSuggestion);
        
        BookSuggestionView view = mapToView(saved);
        bookSuggestionPublisher.publishBookSuggestion(view);
        
        return view;
    }

    private BookSuggestionView mapToView(BookSuggestion suggestion) {
        return BookSuggestionView.builder()
                .id(suggestion.getId())
                .title(suggestion.getTitle())
                .description(suggestion.getDescription())
                .genre(suggestion.getGenre())
                .photoURI(suggestion.getPhotoURI())
                .authors(suggestion.getAuthors())
                .isbn(suggestion.getIsbn().toString())
                .suggestedByReaderId(suggestion.getSuggestedByReaderId())
                .status(suggestion.getStatus())
                .build();
    }
}
