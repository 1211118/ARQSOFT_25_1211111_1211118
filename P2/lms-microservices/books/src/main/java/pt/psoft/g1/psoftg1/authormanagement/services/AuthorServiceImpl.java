package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper mapper;
    private final PhotoRepository photoRepository;
    private final BookEventsPublisher bookEventsPublisher; 


    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findByAuthorNumber(final Long authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.searchByNameNameStartsWith(name);
    }

    @Override
    public Author create(final CreateAuthorRequest resource) {
        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well. To avoid the client
         * sending false data, photoURI has to be set to any value / null according to the MultipartFile photo object
         *
         * That means: - photo = null && photoURI = null -> photo is removed - photo = null && photoURI = validString ->
         * ignored - photo = validFile && photoURI = null -> ignored - photo = validFile && photoURI = validString ->
         * photo is set
         */

        MultipartFile photo = resource.getPhoto();
        String photoURI = resource.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            resource.setPhoto(null);
            resource.setPhotoURI(null);
        }
        final Author author = mapper.create(resource);
        return authorRepository.save(author);
    }

    @Override
    public Author partialUpdate(final Long authorNumber, final UpdateAuthorRequest request, final long desiredVersion) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var author = findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));
        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well. To avoid the client
         * sending false data, photoURI has to be set to any value / null according to the MultipartFile photo object
         *
         * That means: - photo = null && photoURI = null -> photo is removed - photo = null && photoURI = validString ->
         * ignored - photo = validFile && photoURI = null -> ignored - photo = validFile && photoURI = validString ->
         * photo is set
         */

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }
        // since we got the object from the database we can check the version in memory
        // and apply the patch
        author.applyPatch(desiredVersion, request);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object
        return authorRepository.save(author);
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        return bookRepository.findBooksByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        return authorRepository.findCoAuthorsByAuthorNumber(authorNumber);
    }

    @Override
    public Optional<Author> removeAuthorPhoto(Long authorNumber, long desiredVersion) {
        Author author = authorRepository.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = author.getPhoto().getPhotoFile();
        author.removePhoto(desiredVersion);
        Optional<Author> updatedAuthor = Optional.of(authorRepository.save(author));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedAuthor;
    }

    @Override
    public List<Author> validateAuthors(List<String> authorNames) {
        List<Author> bookAuthors = new ArrayList<>();

        for (String authorName : authorNames) {
            // Buscar autores existentes com o nome fornecido
            List<Author> existingAuthors = findByName(authorName);

            // Se não encontrar nenhum autor, cria um novo autor
            Author author = existingAuthors.isEmpty() ? 
                            createNewAuthor(authorName) : 
                            existingAuthors.get(0);

            // Adiciona o autor à lista
            bookAuthors.add(author);
        }

        return bookAuthors;
    }

    @Override
    public Author createNewAuthor(String authorName) {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName(authorName);
        createAuthorRequest.setBio("default bio");
        createAuthorRequest.setPhotoURI(null);
        createAuthorRequest.setPhoto(null);

        // Criar o autor utilizando o serviço
        Author createdAuthor = create(createAuthorRequest);

        // Criar o objeto AuthorViewAMQP para enviar no evento
        AuthorViewAMQP authorView = new AuthorViewAMQP();
        authorView.setName(createdAuthor.getName());
        authorView.setBio(createdAuthor.getBio());
       
        // Enviar evento de criação do autor
        bookEventsPublisher.sendAuthorCreated(authorView);
       
        // Retornar o autor criado
        return createdAuthor;
    }

}
