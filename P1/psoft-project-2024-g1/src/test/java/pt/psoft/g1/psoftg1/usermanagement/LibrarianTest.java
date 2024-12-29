package pt.psoft.g1.psoftg1.usermanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

public class LibrarianTest {

    private Librarian librarian;

    @BeforeEach
    public void setUp() {
        librarian = new Librarian("librarian@example.com", "ValidPassword1");
    }

    @Test
    public void testLibrarianCreation() {
        assertNotNull(librarian);
        assertEquals("librarian@example.com", librarian.getUsername());
        assertTrue(librarian.isEnabled());
    }

    @Test
    public void testAddAuthority() {
        librarian.addAuthority(new Role(Role.LIBRARIAN));
        assertTrue(librarian.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Role.LIBRARIAN)));
    }

    @Test
    public void testPasswordEncryption() {
        String password = "ValidPassword1";
        librarian.setPassword(password);
        assertNotEquals(password, librarian.getPassword()); // A senha deve ser criptografada
        assertTrue(librarian.getPassword().matches("^[\\w\\W]+$")); // Verifica se a senha criptografada não está vazia
    }

    @Test
    public void testAccountNonExpired() {
        assertTrue(librarian.isAccountNonExpired());
    }

    @Test
    public void testAccountNonLocked() {
        assertTrue(librarian.isAccountNonLocked());
    }

    @Test
    public void testCredentialsNonExpired() {
        assertTrue(librarian.isCredentialsNonExpired());
    }


    //RGYHUJIK


    @Test
    void testNewLibrarianWithValidInputs() {
        // Arrange
        String username = "librarian@example.com";
        String password = "Valid1Password!";
        String name = "John Doe";

        // Act
        Librarian librarian = Librarian.newLibrarian(username, password, name);

        // Assert
        assertNotNull(librarian);
        assertEquals(username, librarian.getUsername());
        assertTrue(librarian.isEnabled());
        assertNotNull(librarian.getAuthorities());
        assertTrue(librarian.getAuthorities().contains(new Role(Role.LIBRARIAN)));
        assertEquals(name, librarian.getName().toString()); // Assuming Name class has a proper toString method
    }


    @Test
    void testNewLibrarianWithInvalidPassword() {
        // Arrange
        String username = "librarian@example.com";
        String invalidPassword = "short"; // Invalid password
        String name = "John Doe";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Librarian.newLibrarian(username, invalidPassword, name);
        });
    }

}
