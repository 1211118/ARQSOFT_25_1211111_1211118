package pt.psoft.g1.psoftg1.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreController;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = GenreController.class)
@ContextConfiguration(classes = {GenreController.class, GenreService.class})
@AutoConfigureMockMvc
class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String AUTH_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJleGFtcGxlLmlvIiwic3ViIjoiMSxtYW51ZWxAZ21haWwuY29tIiwiZXhwIjoxNzM2MTI3Nzk5LCJpYXQiOjE3MzYwOTE3OTksInJvbGVzIjoiUkVBREVSIn0.cw848YF7HF_oAviP99zFjhI2t4Pws5UobBvT3dE2NA_yGMz3feZE7vinTr1Me47z76v57StTfz8AiJon6M8A7UwThubmbOV-hu0aAPSbXVx2osj1nQLfCjb3OEEv5ihAm9R0TB9VP9boDbeX7Wxk8N9Dkdi6fkl1GGcuRj5o000TJN19F5Zoh9znA01kHLdEZZUrwXDyTb1HDWYsFsuwWwvx4nA_mdDw4bxZZIGrN_AybkTgEcgtYvTf88hNGeDH4VQs96n4WXsQooh-3wEiorxLZWEoQjucELPEQorXTZKYL2vT_HhY9oNwFUm2ChLut06c425lTDjj4Zym6B-jeFSmZmuuoSC4gea3srx_Wyo7JIP2uU2KEzawDPodnPMsU2oibbQU2d0jQPLOwW91LJQ7YhBTYEPLllAn6fMiHJw8Z9QUqcWnPVxwOnNrNgVRv4X9bBFO4L5elaebVvKLS3itRomCA1O49mP4QxcIlDPeryxNNiM7JqEBBGy7SEfveXefzJ34ghWpY3b5TOIGCDlLVxzihpwXbM6REm0p4o7vR1nOhZDiQA9ycB2gmxmkndIDdvvyrvGh-acvOmKfNPuidBSsfPxbGDM5ub_3UgUF9QTHKdNmSoQOlKKnr_uJM-tEl8PkyVGpeKpIQWPsgRzepZeY45qjfTEtyhumvCc";

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

