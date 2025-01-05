package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@Tag(name = "Genres", description = "Endpoints for managing Genres")
@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    private final GenreViewMapper genreViewMapper;

    @Operation(summary = "Create a new Genre")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GenreView> createGenre(@RequestBody @Valid GenreView genreView) {
        Genre genre = new Genre(genreView.getGenre());
        Genre savedGenre = genreService.save(genre);
        GenreView response = genreViewMapper.toGenreView(savedGenre);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
