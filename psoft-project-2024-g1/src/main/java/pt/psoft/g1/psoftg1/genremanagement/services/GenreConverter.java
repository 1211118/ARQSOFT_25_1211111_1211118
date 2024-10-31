package pt.psoft.g1.psoftg1.genremanagement.services;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.MongoGenre;

public class GenreConverter {

    public static MongoGenre toMongoGenre(Genre genre) {
        MongoGenre mongoGenre = new MongoGenre();
        mongoGenre.setGenre(genre.getGenre());
        return mongoGenre;
    }

    public static Genre toJpaGenre(MongoGenre mongoGenre) {
        Genre genre = new Genre();
        genre.setGenre(mongoGenre.getGenre());
        return genre;
    }
}
