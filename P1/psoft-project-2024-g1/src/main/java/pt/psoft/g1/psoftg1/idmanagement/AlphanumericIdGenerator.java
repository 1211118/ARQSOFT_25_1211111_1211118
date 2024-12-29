package pt.psoft.g1.psoftg1.idmanagement;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component("alphanumericIdGenerator")
public class AlphanumericIdGenerator implements IdGenerator {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateId() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }
}

