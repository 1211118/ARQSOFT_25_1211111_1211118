package pt.psoft.g1.psoftg1.readermanagement.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pt.psoft.g1.psoftg1.readermanagement.model.MongoReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

public class ReaderConverter {

    public static MongoReaderDetails toMongoReaderDetails(ReaderDetails readerDetails) {
        MongoReaderDetails mongoReader = new MongoReaderDetails();

        // Verifica se o ID do Reader não é null antes de tentar definir
        mongoReader.setId(readerDetails.getPk() != null ? readerDetails.getPk().toString() : null);
        
        // Verifica se o ID do Reader não é null antes de definir o readerId
        if (readerDetails.getReader() != null && readerDetails.getReader().getId() != null) {
            mongoReader.setReaderId(readerDetails.getReader().getId().toString());
        } else {
            mongoReader.setReaderId(null);  // Define como null caso o ID não esteja presente
        }

        mongoReader.setReaderNumber(readerDetails.getReaderNumber());
        mongoReader.setBirthDate(readerDetails.getBirthDate().toString());
        mongoReader.setPhoneNumber(readerDetails.getPhoneNumber());
        mongoReader.setGdprConsent(readerDetails.isGdprConsent());
        mongoReader.setMarketingConsent(readerDetails.isMarketingConsent());
        mongoReader.setThirdPartySharingConsent(readerDetails.isThirdPartySharingConsent());
        mongoReader.setVersion(readerDetails.getVersion());
        mongoReader.setInterestList(readerDetails.getInterestList());
        mongoReader.setPhotoURI(readerDetails.getPhoto() != null ? readerDetails.getPhoto().getPhotoFile() : null); 
        
        return mongoReader;
    }

    public static ReaderDetails toReaderDetails(MongoReaderDetails mongoReader) {
        // Extrai apenas o número após a barra no formato 'AAAA/int'
        int readerNumber = extractReaderNumber(mongoReader.getReaderNumber());
    
        // Converte e formata a data de nascimento para o padrão 'yyyy-MM-dd'
        String birthDate = formatBirthDate(mongoReader.getBirthDate());
    
        // Constrói o objeto ReaderDetails
        return new ReaderDetails(
                readerNumber,
                null,  // O leitor será configurado posteriormente
                birthDate,
                mongoReader.getPhoneNumber(),
                mongoReader.isGdprConsent(),
                mongoReader.isMarketingConsent(),
                mongoReader.isThirdPartySharingConsent(),
                mongoReader.getPhotoURI(),
                mongoReader.getInterestList()
        );
    }
    
    // Extrai o número após a barra e converte para int
    private static int extractReaderNumber(String readerNumberStr) {
        try {
            return Integer.parseInt(readerNumberStr.split("/")[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid reader number format. Expected 'AAAA/int'.", e);
        }
    }
    
    // Converte e formata a data de nascimento no padrão 'yyyy-MM-dd'
    private static String formatBirthDate(String birthDateStr) {
        try {
            LocalDate date = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("yyyy-M-d"));
            return date.format(DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birth date format. Use 'yyyy-MM-dd'.", e);
        }
    }

    
}
