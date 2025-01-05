package pt.psoft.g1.psoftg1.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("pt.psoft.g1.psoftg1.booksuggestionManagement.model")
@EnableJpaRepositories(basePackages = {
    "pt.psoft.g1.psoftg1.booksuggestionManagement.repositories",
    "pt.psoft.g1.psoftg1.booksuggestionManagement.infrastructure.repositories.impl"
})
public class JpaConfig {
}
