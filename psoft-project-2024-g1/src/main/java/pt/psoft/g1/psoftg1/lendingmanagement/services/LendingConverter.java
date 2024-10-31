package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.MongoLending;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.Optional;

@Component
public class LendingConverter {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Autowired
    public LendingConverter(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    /**
     * Converte um objeto Lending para MongoLending, preservando as IDs de Book e ReaderDetails.
     * @param lending objeto Lending para conversão
     * @return MongoLending correspondente
     */

     public MongoLending toMongoLending(Lending lending) {
        MongoLending mongoLending = new MongoLending();
        mongoLending.setLendingNumber(lending.getLendingNumber());
        
        // Transfere os IDs de book e readerDetails, sem conversão explícita para String
        mongoLending.setBookId(String.valueOf(lending.getBook().getPk()));
        mongoLending.setReaderDetailsId(String.valueOf(lending.getReaderDetails().getPk()));
        
        // Copia os dados de datas, comentário e valores de multa
        mongoLending.setStartDate(lending.getStartDate());
        mongoLending.setLimitDate(lending.getLimitDate());
        mongoLending.setReturnedDate(lending.getReturnedDate());
        
        // Verifica o campo commentary diretamente
        mongoLending.setCommentary(lending.commentary);
        
        mongoLending.setFineValuePerDayInCents(lending.getFineValuePerDayInCents());
        
        return mongoLending;
    }
    

    /**
     * Converte um objeto MongoLending para Lending. Reconstroi o objeto Lending utilizando Book e ReaderDetails.
     * @param mongoLending objeto MongoLending para conversão
     * @return Lending correspondente
     */
    public Optional<Lending> toLending(MongoLending mongoLending) {
        if (mongoLending == null) {
            return Optional.empty();
        }
    
        // Tenta buscar Book e ReaderDetails a partir dos repositórios
        Optional<Book> bookOpt = bookRepository.findByIsbn(mongoLending.getBookId());
        Optional<ReaderDetails> readerDetailsOpt = readerRepository.findByReaderNumber(mongoLending.getReaderDetailsId());
    
        // Retorna Optional.empty() se Book ou ReaderDetails não estiverem presentes
        if (bookOpt.isEmpty() || readerDetailsOpt.isEmpty()) {
            return Optional.empty();
        }
    
        // Constrói Lending com Book e ReaderDetails existentes
        Lending lending = new Lending(
                bookOpt.get(),
                readerDetailsOpt.get(),
                Integer.parseInt(mongoLending.getLendingNumber()),
                mongoLending.getStartDate().until(mongoLending.getLimitDate()).getDays(),
                mongoLending.getFineValuePerDayInCents()
        );
    
        return Optional.of(lending);
    }
}

