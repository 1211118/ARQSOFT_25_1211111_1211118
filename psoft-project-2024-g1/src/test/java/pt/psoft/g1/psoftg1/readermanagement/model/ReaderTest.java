package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReaderTest {


private Reader mockReader;
    private ReaderDetails readerDetails;
    private List<Genre> initialInterestList;

    @BeforeEach
void setup() {
    mockReader = mock(Reader.class);
    initialInterestList = new ArrayList<>();
    initialInterestList.add(new Genre("Sci-Fi"));
    initialInterestList.add(new Genre("Fantasy"));
    
    readerDetails = new ReaderDetails(123, mockReader, "2000-01-01", "912345678", true, false, false, null, initialInterestList);

    // Define manualmente a versão para evitar NullPointerException
    // Atribuímos, por exemplo, o valor 1L, mas pode ser qualquer valor
    setVersion(readerDetails, 1L);
}

// Método auxiliar para definir a versão usando reflexão
private void setVersion(ReaderDetails readerDetails, Long version) {
    try {
        Field versionField = ReaderDetails.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(readerDetails, version);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


    @Test
    void ensureValidReaderDetailsAreCreated() {
        Reader mockReader = mock(Reader.class);
        assertDoesNotThrow(() -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null, null));
    }

    @Test
    void ensureExceptionIsThrownForNullReader() {
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, null, "2010-01-01", "912345678", true, false, false,null,null));
    }

    @Test
    void ensureExceptionIsThrownForNullPhoneNumber() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", null, true, false, false,null,null));
    }

    @Test
    void ensureExceptionIsThrownForNoGdprConsent() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", false, false, false,null,null));
    }

    @Test
    void ensureGdprConsentIsTrue() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null,null);
        assertTrue(readerDetails.isGdprConsent());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null,null);
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",null);
        Photo photo = readerDetails.getPhoto();

        //This is here to force the test to fail if the photo is null
        assertNotNull(photo);

        String readerPhoto = photo.getPhotoFile();
        assertEquals("readerPhotoTest.jpg", readerPhoto);
    }

    @Test
    void ensureInterestListCanBeNullOrEmptyList_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetailsNullInterestList = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",null);
        assertNull(readerDetailsNullInterestList.getInterestList());

        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",new ArrayList<>());
        assertEquals(0, readerDetailsInterestListEmpty.getInterestList().size());
    }

    @Test
    void ensureInterestListCanTakeAnyValidGenre() {
        Reader mockReader = mock(Reader.class);
        Genre g1 = new Genre("genre1");
        Genre g2 = new Genre("genre2");
        List<Genre> genreList = new ArrayList<>();
        genreList.add(g1);
        genreList.add(g2);

        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",genreList);
        assertEquals(2, readerDetails.getInterestList().size());
    }

    @Test
void ensureMarketingConsentCanBeUpdated() {
    Reader mockReader = mock(Reader.class);
    ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, null, null);

    readerDetails.setMarketingConsent(true);

    assertTrue(readerDetails.isMarketingConsent());
}

@Test
void ensureThirdPartySharingConsentCanBeUpdated() {
    Reader mockReader = mock(Reader.class);
    ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, null, null);

    readerDetails.setThirdPartySharingConsent(true);

    assertTrue(readerDetails.isThirdPartySharingConsent());
}

@Test
void ensureReaderNumberIsGeneratedCorrectlyWithYear() {
    ReaderNumber readerNumber = new ReaderNumber(2020, 100);

    assertEquals("2020/100", readerNumber.toString());
}

@Test
void ensureReaderNumberUsesCurrentYearWhenNotSpecified() {
    int currentYear = LocalDate.now().getYear();
    ReaderNumber readerNumber = new ReaderNumber(100);

    assertEquals(currentYear + "/100", readerNumber.toString());
}

@Test
void ensureExceptionIsThrownForInvalidBirthDateFormat() {
    assertThrows(IllegalArgumentException.class, () -> new BirthDate("20-01-2000"));
}

//Unit Transparent-box tests

@Test
void constructorThrowsExceptionForNullReader() {
    // Caminho interno onde o construtor verifica se o Reader é null
    assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, null, "2000-01-01", "912345678", true, false, false, null, null));
}

@Test
void constructorThrowsExceptionForNoGdprConsent() {
    // Testa o caminho onde o GDPR é falso, o que deve lançar uma exceção
    Reader mockReader = mock(Reader.class);
    assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2000-01-01", "912345678", false, false, false, null, null));
}

@Test
void testSetReaderNumberUsingReflection() throws Exception {
    Reader mockReader = mock(Reader.class);
    ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2000-01-01", "912345678", true, false, false, null, null);

    // Usa reflexão para acessar o método privado
    Method setReaderNumberMethod = ReaderDetails.class.getDeclaredMethod("setReaderNumber", ReaderNumber.class);
    setReaderNumberMethod.setAccessible(true); 

    ReaderNumber newReaderNumber = new ReaderNumber(2020, 456);
    setReaderNumberMethod.invoke(readerDetails, newReaderNumber); 

    
    assertEquals("2020/456", readerDetails.getReaderNumber());
}

@Test
void constructorThrowsExceptionForNullPhoneNumber() {
    Reader mockReader = mock(Reader.class);
    
    assertThrows(IllegalArgumentException.class, () -> 
        new ReaderDetails(123, mockReader, "2000-01-01", null, true, false, false, null, null)
    );
}

// ......................

@Test
void testApplyPatch_UpdatesFieldsCorrectly() {
    // Configura o request com os dados a serem atualizados
    UpdateReaderRequest request = mock(UpdateReaderRequest.class);
    when(request.getBirthDate()).thenReturn("1990-05-15");
    when(request.getPhoneNumber()).thenReturn("987654321");
    when(request.getMarketing()).thenReturn(true);
    when(request.getThirdParty()).thenReturn(true);
    when(request.getFullName()).thenReturn("John Doe");
    when(request.getUsername()).thenReturn("johndoe");
    when(request.getPassword()).thenReturn("securepassword");

    List<Genre> updatedInterestList = new ArrayList<>();
    updatedInterestList.add(new Genre("Mystery"));
    updatedInterestList.add(new Genre("Drama"));

    // Atualiza a versão para corresponder ao valor correto
    long currentVersion = readerDetails.getVersion();
    
    // Executa o patch com a versão correta
    assertDoesNotThrow(() -> readerDetails.applyPatch(currentVersion, request, "newPhoto.jpg", updatedInterestList));

    // Verifica as atualizações de campo
    assertEquals("1990-5-15", readerDetails.getBirthDate().toString());
    assertEquals("987654321", readerDetails.getPhoneNumber());
    assertTrue(readerDetails.isMarketingConsent());
    assertTrue(readerDetails.isThirdPartySharingConsent());
    assertEquals("newPhoto.jpg", readerDetails.getPhoto().getPhotoFile());
    assertEquals(2, readerDetails.getInterestList().size());
}

@Test
void testApplyPatch_ThrowsConflictExceptionForVersionMismatch() {
    // Configura o request com dados para o teste de versão
    UpdateReaderRequest request = mock(UpdateReaderRequest.class);
    when(request.getMarketing()).thenReturn(true);
    
    long incorrectVersion = readerDetails.getVersion() + 1;

    // Espera que a exceção ConflictException seja lançada devido ao conflito de versão
    assertThrows(ConflictException.class, () -> 
        readerDetails.applyPatch(incorrectVersion, request, null, null)
    );
}

@Test
void testRemovePhoto_RemovesPhotoWhenVersionMatches() {
    // Configura a versão atual correta
    long currentVersion = readerDetails.getVersion();
    
    // Remove a foto com a versão correta
    assertDoesNotThrow(() -> readerDetails.removePhoto(currentVersion));

    // Verifica se a foto foi removida (getPhoto deve retornar null)
    assertNull(readerDetails.getPhoto());
}

@Test
void testRemovePhoto_ThrowsConflictExceptionForVersionMismatch() {
    // Configura uma versão incorreta para o teste de conflito de versão
    long incorrectVersion = readerDetails.getVersion() + 1;

    // Espera que a exceção ConflictException seja lançada devido ao conflito de versão
    assertThrows(ConflictException.class, () -> readerDetails.removePhoto(incorrectVersion));
}

@Test
void testGetPhoneNumber_ReturnsFormattedPhoneNumber() {
    // Cria um objeto PhoneNumber esperado e verifica se o formato é correto
    String expectedPhoneNumber = "912345678";
    
    // Confirma que o método getPhoneNumber retorna o valor esperado
    assertEquals(expectedPhoneNumber, readerDetails.getPhoneNumber());
}

@Test
void testApplyPatch_OnlyUpdatesNonNullFields() {
    // Configura o request com apenas alguns campos preenchidos
    UpdateReaderRequest request = mock(UpdateReaderRequest.class);
    when(request.getMarketing()).thenReturn(false);  
    when(request.getThirdParty()).thenReturn(true); 
    when(request.getPhoneNumber()).thenReturn(null); 
    when(request.getFullName()).thenReturn(null);    

    long currentVersion = readerDetails.getVersion();

    // Executa o patch
    assertDoesNotThrow(() -> readerDetails.applyPatch(currentVersion, request, null, null));

    // Verifica que apenas os campos não-nulos foram atualizados
    assertFalse(readerDetails.isMarketingConsent());
    assertTrue(readerDetails.isThirdPartySharingConsent());
    assertEquals("912345678", readerDetails.getPhoneNumber()); // Não alterado
    assertEquals("Sci-Fi", readerDetails.getInterestList().get(0).getGenre()); // Não alterado
}

    
}
