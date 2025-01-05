package pt.psoft.g1.psoftg1.bookmanagement.services;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookWithAuthorAndGenreRequest {
    @NotNull
    private String title;
    @NotNull
    private String isbn;
    private String description;
    @NotNull
    private String genre;
    @NotNull
    private String authorName;
    private String authorBio;
}
