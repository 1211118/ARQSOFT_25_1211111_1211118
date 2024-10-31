package pt.psoft.g1.psoftg1.usermanagement.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.model.Name;

@Document(collection = "users")
public class MongoUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    private String id;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime modifiedAt;

    @Getter
    private String createdBy;

    private String modifiedBy;

    @Getter @Setter
    private boolean enabled = true;

    @Getter @Setter
    private String username;

    @Getter
    private String password;

    @Getter @Setter
    private Name name;

    @Getter
    private Set<Role> authorities = new HashSet<>();

    // PasswordEncoder não é diretamente injetado no MongoUser
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Construtor padrão necessário para o MongoDB
    public MongoUser() {
    }

    // Construtor com parâmetros para uso explícito no código
    public MongoUser(String username, String password) {
        this.username = username;
        setPassword(password); 
    }

    public void setPassword(final String password) {
		this.password = password; // Armazena a senha em texto puro para teste
	}

    // Implementação dos métodos de UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Métodos adicionais para manipulação de authorities
    public void addAuthority(Role role) {
        authorities.add(role);
    }
}

