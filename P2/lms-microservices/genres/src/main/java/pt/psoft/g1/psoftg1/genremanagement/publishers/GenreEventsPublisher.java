package pt.psoft.g1.psoftg1.genremanagement.publishers;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

public interface GenreEventsPublisher {

    void sendGenreCreated(Genre genre);

    void sendGenreUpdated(Genre genre, Long currentVersion);

    void sendGenreDeleted(Genre genre, Long currentVersion);
}