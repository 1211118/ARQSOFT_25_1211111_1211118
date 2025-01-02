package pt.psoft.g1.psoftg1.bookSuggestionManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookSuggestionManagement.model.BookSuggestion;

@Repository
public interface BookSuggestionRepository extends JpaRepository<BookSuggestion, Long> {
    BookSuggestion save(BookSuggestion bookSuggestion);

}