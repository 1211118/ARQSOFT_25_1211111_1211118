package pt.psoft.g1.psoftg1.bookmanagement.api;

import lombok.Data;

@Data
public class AuthorViewAMQP {
    
    private String name;
    private String bio;
    private String photoURI;
}