package pt.psoft.g1.psoftg1.readermanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReaderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReaderService readerService;

    @Mock
    private UserService userService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private ReaderViewMapper readerViewMapper;

    @InjectMocks
    private ReaderController readerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(readerController).build();
    }

    @Test
    void getTopReaders_shouldReturnListOfReaders_whenTopReadersExist() throws Exception {
        List<ReaderDetails> mockTopReaders = List.of(mock(ReaderDetails.class));
        when(readerService.findTopReaders(5)).thenReturn(mockTopReaders);
        when(readerViewMapper.toReaderView(mockTopReaders)).thenReturn(List.of(new ReaderView()));

        mockMvc.perform(get("/api/readers/top5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTop5ReaderByGenre_shouldReturnListOfReaders_whenTopReadersByGenreExist() throws Exception {
        when(readerService.findTopByGenre(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(new ReaderBookCountDTO()));

        mockMvc.perform(get("/api/readers/top5ByGenre")
                        .param("genre", "Fiction")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void searchReaders_shouldReturnListOfReaders_whenMatchingReadersExist() throws Exception {
        SearchReadersQuery query = new SearchReadersQuery();
        List<ReaderDetails> mockReaderList = List.of(mock(ReaderDetails.class));
        when(readerService.searchReaders(any(), any())).thenReturn(mockReaderList);
        when(readerViewMapper.toReaderView(mockReaderList)).thenReturn(List.of(new ReaderView()));

        mockMvc.perform(post("/api/readers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"page\": {\"number\": 1, \"limit\": 10}, \"query\": {\"name\": \"Manuel\"}}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void findByReaderName_shouldReturnReader_whenReaderExists() throws Exception {
        User mockUser = mock(User.class);
        when(userService.findByNameLike("Manuel")).thenReturn(List.of(mockUser));

        ReaderDetails mockReaderDetails = mock(ReaderDetails.class);
        when(readerService.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockReaderDetails));
        when(readerViewMapper.toReaderView(anyList())).thenReturn(List.of(new ReaderView()));

        mockMvc.perform(get("/api/readers").param("name", "Manuel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void findByPhoneNumber_shouldReturnReader_whenReaderExists() throws Exception {
        ReaderDetails mockReaderDetails = mock(ReaderDetails.class);
        when(readerService.findByPhoneNumber("919191919")).thenReturn(List.of(mockReaderDetails));
        when(readerViewMapper.toReaderView(anyList())).thenReturn(List.of(new ReaderView()));

        mockMvc.perform(get("/api/readers").param("phoneNumber", "919191919"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
