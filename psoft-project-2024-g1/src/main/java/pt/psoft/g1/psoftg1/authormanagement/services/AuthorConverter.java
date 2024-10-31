package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;

public class AuthorConverter {

    public static AuthorMongo toMongo(Author author) {
        return new AuthorMongo(
            author.getName(),
            author.getBio(),
            null
        );
    }

    public static Author toJpa(AuthorMongo authorMongo) {
        return new Author(
            authorMongo.getName(),
            authorMongo.getBio(),
            null
        );
    }
}
