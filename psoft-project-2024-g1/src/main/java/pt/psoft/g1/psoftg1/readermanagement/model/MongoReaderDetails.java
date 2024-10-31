package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Document(collection = "reader_details")  // Define a coleção no MongoDB
@Getter
@Setter
public class MongoReaderDetails {

    @Id  // Identificador do documento no MongoDB
    private String id;

    private String readerId;                // ID do leitor (link para a entidade Reader)
    private String readerNumber;            // Número único do leitor
    private String birthDate;               // Data de nascimento no formato String
    private String phoneNumber;             // Número de telefone no formato String
    
    private boolean gdprConsent;            // Consentimento para GDPR
    private boolean marketingConsent;       // Consentimento para marketing
    private boolean thirdPartySharingConsent; // Consentimento para compartilhamento com terceiros

    private Long version;                   // Controle de versão do documento

    private List<Genre> interestList;       // Lista de interesses (gêneros)

    private String photoURI;                // URI para a foto do leitor

    // Construtor vazio para uso do MongoDB
    public MongoReaderDetails() {}

    // Construtor completo para inicializar todos os campos (opcional)
    public MongoReaderDetails(String id, String readerId, String readerNumber, String birthDate, 
                              String phoneNumber, boolean gdprConsent, boolean marketingConsent, 
                              boolean thirdPartySharingConsent, Long version, List<Genre> interestList, 
                              String photoURI) {
        this.id = id;
        this.readerId = readerId;
        this.readerNumber = readerNumber;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gdprConsent = gdprConsent;
        this.marketingConsent = marketingConsent;
        this.thirdPartySharingConsent = thirdPartySharingConsent;
        this.version = version;
        this.interestList = interestList;
        this.photoURI = photoURI;
    }
}
