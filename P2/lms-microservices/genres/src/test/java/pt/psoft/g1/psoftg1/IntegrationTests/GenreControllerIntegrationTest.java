package pt.psoft.g1.psoftg1.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String AUTH_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJleGFtcGxlLmlvIiwic3ViIjoiMSxtYW51ZWxAZ21haWwuY29tIiwiZXhwIjoxNzM2MDMxNDYwLCJpYXQiOjE3MzU5OTU0NjAsInJvbGVzIjoiUkVBREVSIn0.fUUdqKo-DgzuWKe07y-HZnWGgD0Y2o8phje5PwwPUfacenRoRZlzZsfW1cWz8_5mzkpPYSO8R4rEch3PilPua2b1OXUwrs8vOj3SdBKUl4zVn98TeW_445p39bghcCI8sKj6QA_I3wqDx7_8LVdCI9x8N2lYe8XjzVIC1AbNkpTRX1oQMeEkWZRv8-jR-p99sOKwTWoMEksbb6nD1cAKChuX9uoOfPbMpkMnKs1E0DF_TNF1yy8Gxxhzw9bAY7k9uCsbAjXKQSAn3cfiJzZ3Z2nkFX1NJtXAzI9Ekvb8mYlJJRKJG0GZpfqwbzKQ1LMHCGDOB98INlgXV5xeoUVM--xrgCS1frycGDZje2Wj6lfI7t3lptAapyeIASeUwEDMCZMwvP0_NNd3Tp_WPIupIm1ojFh__kd6W3eAy_U1QNDxSczjGpzOYIh4JP7b8yMY7CbN8lw417ze6m6jXwZCjhPeYnNlXHW8q9WLLeqgvG_9vJHgqcKj5YhqdrCL0FHBXaqgolIP2q5yotUM5UBZKDjqZD0cHO1kPZG7Gwb9IShDfF1tZTeUy7LwrQ2s8khZun27w5dTC5crSGbbRmHUjQg9zROuetK9lMRIYgJKbE6U2r8gLaSQtVkTkEbMICntvbGTV1oJ56AGNzeTudchi0g7PYQeTGDqX6mHjrzfDDw";

    @Test
    void shouldCreateGenreSuccessfully() throws Exception {
        String genreJson = """
                {
                    "genre": "Science Fiction"
                }
                """;

        mockMvc.perform(post("/api/genres")
                        .contentType("application/json")
                        .content(genreJson)
                        .header("Authorization", "Bearer " + AUTH_TOKEN))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.genre").value("Science Fiction"));
    }
}

