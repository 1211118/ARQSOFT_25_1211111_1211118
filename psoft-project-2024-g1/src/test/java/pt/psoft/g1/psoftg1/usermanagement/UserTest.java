package pt.psoft.g1.psoftg1.usermanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.Password;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testuser@example.com", "ValidPass1!");
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user);
        assertEquals("testuser@example.com", user.getUsername());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testSetPassword_Success() {
        user.setPassword("NewValidPass1!");
        assertNotNull(user.getPassword());
        assertNotEquals("NewValidPass1!", user.getPassword()); // Ensure password is hashed
    }

    @Test
    public void testSetPassword_InvalidPassword_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword("short");
        });
        assertEquals("Given Password is not valid. It must contain at least 8 characters, 1 upper case letter, 1 lower case letter and 1 number or special character.", exception.getMessage());
    }

    @Test
    public void testAddAuthority() {
        Role role = new Role(Role.ADMIN);
        user.addAuthority(role);
        assertTrue(user.getAuthorities().contains(role));
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testNewUserWithRoleFactoryMethod() {
        User newUser = User.newUser("admin@example.com", "ValidPass1!", "Admin User", Role.ADMIN);
        assertEquals("admin@example.com", newUser.getUsername());
        assertTrue(newUser.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Role.ADMIN)));
    }

    @Test
    public void testSetEnabled() {
        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    public void testGetAuthorities_InitiallyEmpty() {
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    public void testConstructorWithUsernameAndPassword() {
        assertEquals("testuser@example.com", user.getUsername());
        assertNotNull(user.getPassword());
    }

    @Test
    public void testPasswordValidation_EmptyPassword_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword("");
        });
        assertEquals("Given Password is not valid. It must contain at least 8 characters, 1 upper case letter, 1 lower case letter and 1 number or special character.", exception.getMessage());
    }

    @Test
    public void testPasswordValidation_NullPassword_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword(null);
        });
        assertEquals("Given Password is not valid. It must contain at least 8 characters, 1 upper case letter, 1 lower case letter and 1 number or special character.", exception.getMessage());
    }



}
