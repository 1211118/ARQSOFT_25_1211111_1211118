package pt.psoft.g1.psoftg1.booksuggestion.infrastructure.repositories.impl;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.model.BookSuggestion;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.repositories.BookSuggestionRepository;

@Repository
public interface SpringDataBookSuggestionRepository extends BookSuggestionRepository {
    @Override
    BookSuggestion save(BookSuggestion bookSuggestion);
}

