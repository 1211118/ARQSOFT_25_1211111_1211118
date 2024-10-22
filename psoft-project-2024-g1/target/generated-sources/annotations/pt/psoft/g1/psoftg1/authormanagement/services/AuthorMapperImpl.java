package pt.psoft.g1.psoftg1.authormanagement.services;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T16:26:26+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.v20240919-1711, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class AuthorMapperImpl extends AuthorMapper {

    @Override
    public Author create(CreateAuthorRequest request) {
        if ( request == null ) {
            return null;
        }

        String photo = null;
        String name = null;
        String bio = null;

        photo = map( request.getPhotoURI() );
        name = map( request.getName() );
        bio = map( request.getBio() );

        Author author = new Author( name, bio, photo );

        return author;
    }

    @Override
    public void update(UpdateAuthorRequest request, Author author) {
        if ( request == null ) {
            return;
        }

        author.setPhoto( map( request.getPhoto() ) );
        author.setName( map( request.getName() ) );
        author.setBio( map( request.getBio() ) );
    }
}
