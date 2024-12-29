package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;

//Unit Opaque-box tests for Author Domain Class

class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }
    @BeforeEach
    void setUp() {
    }
    @Test
    void ensureNameNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(null,validBio, null));
    }

    @Test
    void ensureBioNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(validName,null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName,validBio, null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(request.getName(), request.getBio(), null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }

    // Unit Opaque-box tests 

    @Test
void testPartialUpdateWithApplyPatch() {
    Author author = new Author("Carlos", "Original bio", "photo.jpg");
    UpdateAuthorRequest partialUpdateRequest = new UpdateAuthorRequest("Updated bio", null, null, null);
    
    author.applyPatch(author.getVersion(), partialUpdateRequest);
    
    assertEquals("Carlos", author.getName()); // Nome permanece o mesmo
    assertEquals("Updated bio", author.getBio()); // Bio foi atualizada
    assertNotNull(author.getPhoto()); // Foto permanece a mesma
    assertEquals("photo.jpg", author.getPhoto().getPhotoFile());
}

@Test
void testCompleteUpdateWithApplyPatch() {
    Author author = new Author("Carlos", "Original bio", "photo.jpg");
    UpdateAuthorRequest completeUpdateRequest = new UpdateAuthorRequest("Updated bio", "Updated Name", null, "newPhoto.jpg");
    
    author.applyPatch(author.getVersion(), completeUpdateRequest);
    
    assertEquals("Updated Name", author.getName()); // Nome atualizado
    assertEquals("Updated bio", author.getBio()); // Bio atualizada
    assertNotNull(author.getPhoto()); // Foto foi atualizada
    assertEquals("newPhoto.jpg", author.getPhoto().getPhotoFile());
}

@Test
void testRemovePhotoWithCorrectVersion() {
    Author author = new Author("Carlos", "Bio", "photo.jpg");
    
    author.removePhoto(author.getVersion()); // Remove foto com versão correta
    
    assertNull(author.getPhoto()); // Foto deve ser nula após remoção
}

@Test
void testRemovePhotoWithIncorrectVersion() {
    Author author = new Author("Carlos", "Bio", "photo.jpg");
    
    assertThrows(ConflictException.class, () -> author.removePhoto(999L)); // Versão incorreta gera exceção
}


@Test
void testApplyPatchWithIncorrectVersion() {
    Author author = new Author("Carlos", "Original bio", null);
    UpdateAuthorRequest request = new UpdateAuthorRequest("New Name", "New bio", null, null);
    
    assertThrows(StaleObjectStateException.class, () -> author.applyPatch(999L, request));
}

@Test
void testBioSanitization() {
    String maliciousBio = "<script>alert('hack');</script> Clean bio text.";
    Author author = new Author("Carlos", maliciousBio, null);
    
    assertFalse(author.getBio().contains("<script>")); // Verifica se o script foi removido
    assertEquals(" Clean bio text.", author.getBio()); // Verifica o texto sanitizado
}

@Test
void testAuthorCreationWithValidPhoto() {
    Author author = new Author("Ana Maria", "Bio breve", "photo.jpg");
    
    assertNotNull(author); // Autor foi criado com sucesso
    assertEquals("Ana Maria", author.getName());
    assertEquals("Bio breve", author.getBio());
    assertNotNull(author.getPhoto()); // Foto foi adicionada corretamente
    assertEquals("photo.jpg", author.getPhoto().getPhotoFile());
}

@Test
void testAuthorCreationWithNullPhoto() {
    Author author = new Author("Ana Maria", "Bio breve", null);
    
    assertNotNull(author); // Autor foi criado com sucesso
    assertEquals("Ana Maria", author.getName());
    assertEquals("Bio breve", author.getBio());
    assertNull(author.getPhoto()); // Foto é opcional e deve ser nula
}

    // Unit Transparent-box tests

    @Test
    void whenApplyingPatch_thenVersionIsUpdated() {
        Author author = new Author("Original Name", "Original Bio", "photo.jpg");
        long initialVersion = author.getVersion();
    
        UpdateAuthorRequest request = new UpdateAuthorRequest("Updated Bio", "Updated Name", null, null);
        author.applyPatch(initialVersion, request);
    
        // Verificação do estado interno da versão
        assertFalse(author.getVersion() > initialVersion);
        assertEquals("Updated Name", author.getName());
        assertEquals("Updated Bio", author.getBio());
    }

    @Test
void whenApplyingPatchWithPartialUpdate_thenOnlySpecifiedFieldsAreUpdated() {
    Author author = new Author("Original Name", "Original Bio", "photo.jpg");
    long initialVersion = author.getVersion();

    // Atualizar apenas o nome
    UpdateAuthorRequest request = new UpdateAuthorRequest(null, "Updated Name", null, null);
    author.applyPatch(initialVersion, request);

    // Verificação de estados internos
    assertEquals("Updated Name", author.getName()); // Nome deve ser atualizado
    assertEquals("Original Bio", author.getBio()); // Bio deve permanecer inalterada
    assertEquals("photo.jpg", author.getPhoto().getPhotoFile()); // Foto deve permanecer inalterada
}

@Test
void whenRemovingPhoto_thenPhotoIsSetToNull() {
    Author author = new Author("Name", "Bio", "photo.jpg");
    long initialVersion = author.getVersion();

    // Remover a foto e verificar o estado intermediário
    author.removePhoto(initialVersion);
    assertNull(author.getPhoto()); // Verifica o estado interno da foto
}

@Test
void whenSettingName_thenInternalNameObjectIsUpdated() {
    Author author = new Author("Original Name", "Bio", null);

    author.setName("Updated Name");

    // Verifica o estado interno do objeto `Name`
    assertNotNull(author.getName()); // Confirma que o objeto Name não é nulo
    assertEquals("Updated Name", author.getName().toString()); // Verifica a atualização interna
}

@Test
void whenApplyingMultiplePatches_thenNameAndBioAreUpdatedConsistently() {
    Author author = new Author("Initial Name", "Initial Bio", null);
    long initialVersion = author.getVersion();

    // Primeiro patch para atualizar o nome
    UpdateAuthorRequest firstRequest = new UpdateAuthorRequest(null, "Updated Name", null, null);
    author.applyPatch(initialVersion, firstRequest);
    
    // Verifica se o estado interno do nome foi atualizado corretamente
    assertEquals("Updated Name", author.getName());
    
    // Segundo patch para atualizar a bio
    long newVersion = author.getVersion();
    UpdateAuthorRequest secondRequest = new UpdateAuthorRequest("Updated Bio", null, null, null);
    author.applyPatch(newVersion, secondRequest);

    // Verifica se o estado interno da bio foi atualizado corretamente
    assertEquals("Updated Bio", author.getBio());
}

@Test
void whenRemovingPhoto_thenVersionRemainsUnchanged() {
    Author author = new Author("Name", "Bio", "photo.jpg");
    long initialVersion = author.getVersion();

    // Remover a foto e verificar o estado da versão
    author.removePhoto(initialVersion);

    // Foto deve ser removida, mas a versão deve permanecer a mesma
    assertNull(author.getPhoto());
    assertEquals(initialVersion, author.getVersion());
}

@Test
void whenCreatingAuthor_thenInternalStateIsConsistent() {
    Author author = new Author("Name Example", "Bio Example", "photo.jpg");

    // Verifica o estado interno logo após a criação
    assertEquals("Name Example", author.getName());
    assertEquals("Bio Example", author.getBio());
    assertNotNull(author.getPhoto());
    assertEquals("photo.jpg", author.getPhoto().getPhotoFile());
    assertEquals(0, author.getVersion()); // Versão inicial deve ser zero
}

@Test
void whenApplyingPatchWithNewPhoto_thenPhotoStateIsUpdated() {
    Author author = new Author("Name", "Bio", "originalPhoto.jpg");
    long initialVersion = author.getVersion();

    // Aplica um patch que atualiza a URI da foto
    UpdateAuthorRequest request = new UpdateAuthorRequest(null, null, null, "newPhoto.jpg");
    author.applyPatch(initialVersion, request);

    // Verifica o estado interno da foto
    assertNotNull(author.getPhoto());
    assertEquals("newPhoto.jpg", author.getPhoto().getPhotoFile());
}

}

