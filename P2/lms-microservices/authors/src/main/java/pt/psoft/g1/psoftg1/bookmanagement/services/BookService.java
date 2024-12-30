package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;

/**
 *
 */
public interface BookService {

    Book create(CreateBookRequest request, String isbn); // REST request


    Book findByIsbn(String isbn);

    Book update(UpdateBookRequest request, Long currentVersion);

    List<Book> findByTitle(String title);

    List<Book> findByAuthorName(String authorName);

    Book removeBookPhoto(String isbn, long desiredVersion);

    List<Book> searchBooks(Page page, SearchBooksQuery query);
}
