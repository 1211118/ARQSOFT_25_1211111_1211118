package pt.psoft.g1.psoftg1.authormanagement.model;

public class BioMongo {

    private String bio;

    public BioMongo(String bio) {
        setBio(bio);
    }

    protected BioMongo() {
    }

    public void setBio(String bio) {
        this.bio = bio; // Sanitização HTML, se necessário
    }

    @Override
    public String toString() {
        return bio;
    }
}
