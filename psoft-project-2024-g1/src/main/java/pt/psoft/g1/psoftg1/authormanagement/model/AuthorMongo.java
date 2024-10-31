package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.Photo;

@Document(collection = "authors") // MongoDB collection
public class AuthorMongo extends EntityWithPhoto {
    @Id
    private String id;

    @Getter
    private Long authorNumber; // MongoDB IDs são Strings (ObjectId)

    @Version
    private long version; // Controle de versão para atualizações concorrentes

    private Name name;

    private Bio bio;

    public void setName(String name) {
        this.name = new Name(name);
    }

    public void setBio(String bio) {
        this.bio = new Bio(bio);
    }

    public void setAuthorNumber(Long authorNumber) {
        this.authorNumber = authorNumber;
    }

    public Long getVersion() {
        return version;
    }

    public Long getId() {
        return authorNumber;
    }

    public AuthorMongo(String name, String bio, String photo) {

        setName(name);
        setBio(bio);
        setAuthorNumber(1L);
        setPhoto(photo);
    }

    protected AuthorMongo() {
        // got ORM only
    }

    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion)
            throw new ConflictException("Object was already modified by another user");
        if (request.getName() != null)
            setName(request.getName());
        if (request.getBio() != null)
            setBio(request.getBio());
        if(request.getPhotoURI() != null)
            setPhotoInternal(request.getPhotoURI());
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }
    public String getName() {
        return this.name.toString();
    }

    public String getBio() {
        return this.bio.toString();
    }
}