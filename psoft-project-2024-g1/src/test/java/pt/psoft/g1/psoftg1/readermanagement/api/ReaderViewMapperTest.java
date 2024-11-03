package pt.psoft.g1.psoftg1.readermanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReaderViewMapperTest {

    private ReaderViewMapper readerViewMapper;

    @BeforeEach
    void setUp() {
        readerViewMapper = Mappers.getMapper(ReaderViewMapper.class);
    }


    @Test
    void toReaderView_shouldMapListOfReaderDetailsToListOfReaderView() {
        Reader reader1 = new Reader("manuel@gmail.com", "Manuelino123!");
        reader1.setName("Manuel Sarapinto");
        Reader reader2 = new Reader("joao@gmail.com", "JoaoRatao123!");
        reader2.setName("João Ratao");

        Genre genre1 = new Genre("Ficção Científica");
        Genre genre2 = new Genre("Drama");

        ReaderDetails readerDetails1 = new ReaderDetails(1, reader1, "2000-01-01", "919191919", true, true, true, null, List.of(genre1));
        ReaderDetails readerDetails2 = new ReaderDetails(2, reader2, "1995-06-02", "929292929", true, false, false, null, List.of(genre2));

        List<ReaderView> readerViews = readerViewMapper.toReaderView(List.of(readerDetails1, readerDetails2));

        assertThat(readerViews).hasSize(2);
    }

    @Test
    void toReaderCountView_shouldMapReaderBookCountDTOToReaderCountView() {
        Reader reader = new Reader("catarina@gmail.com", "Catarina123!");
        reader.setName("Catarina Martins");
        
        Genre genre = new Genre("Aventura");
        ReaderDetails readerDetails = new ReaderDetails(3, reader, "2002-05-21", "939393939", true, true, false, null, List.of(genre));

        ReaderBookCountDTO readerBookCountDTO = new ReaderBookCountDTO(readerDetails, 10L);

        ReaderCountView readerCountView = readerViewMapper.toReaderCountView(readerBookCountDTO);

        assertThat(readerCountView.getLendingCount()).isEqualTo(10L);
    }

    @Test
    void toReaderCountViewList_shouldMapListOfReaderBookCountDTOToListOfReaderCountView() {
        Reader reader1 = new Reader("pedro@gmail.com", "Pedro123!");
        reader1.setName("Pedro Silva");
        Reader reader2 = new Reader("luis@gmail.com", "Luis123!");
        reader2.setName("Luis Oliveira");

        Genre genre1 = new Genre("Aventura");
        Genre genre2 = new Genre("Comédia");

        ReaderDetails readerDetails1 = new ReaderDetails(1, reader1, "1998-04-10", "919111111", true, false, true, null, List.of(genre1));
        ReaderDetails readerDetails2 = new ReaderDetails(2, reader2, "2000-12-12", "919222222", true, true, false, null, List.of(genre2));

        ReaderBookCountDTO readerBookCountDTO1 = new ReaderBookCountDTO(readerDetails1, 15L);
        ReaderBookCountDTO readerBookCountDTO2 = new ReaderBookCountDTO(readerDetails2, 5L);

        List<ReaderCountView> readerCountViews = readerViewMapper.toReaderCountViewList(List.of(readerBookCountDTO1, readerBookCountDTO2));

        assertThat(readerCountViews).hasSize(2);
        assertThat(readerCountViews.get(0).getLendingCount()).isEqualTo(15L);
        assertThat(readerCountViews.get(1).getLendingCount()).isEqualTo(5L);
    }
}
