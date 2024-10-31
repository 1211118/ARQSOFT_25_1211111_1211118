package pt.psoft.g1.psoftg1.idmanagement;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component("hexadecimalIdGenerator")
public class HexadecimalIdGenerator implements IdGenerator {
    
    @Override
    public String generateId() {
        SecureRandom random = new SecureRandom();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            hexString.append(String.format("%x", random.nextInt(16)));
        }
        return hexString.toString();
    }
}
