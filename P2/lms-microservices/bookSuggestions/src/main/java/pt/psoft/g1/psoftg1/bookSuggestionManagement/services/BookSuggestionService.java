package pt.psoft.g1.psoftg1.bookSuggestionManagement.services;

import pt.psoft.g1.psoftg1.bookSuggestionManagement.api.BookSuggestionView;

public interface BookSuggestionService {
    BookSuggestionView suggestBook(CreateBookSuggestionRequest request);
}
