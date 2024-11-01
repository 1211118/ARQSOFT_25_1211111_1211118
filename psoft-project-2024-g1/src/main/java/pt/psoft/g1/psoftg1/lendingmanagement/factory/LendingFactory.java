package pt.psoft.g1.psoftg1.lendingmanagement.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.idmanagement.IdGenerator;

import java.time.LocalDate;

@Component
public class LendingFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${idGenerator}")
    private String selectedIdGenerator;

    /**
     * Cria uma nova instância de Lending com o ID gerado.
     */
    public Lending createLending(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents) {
        IdGenerator idGenerator = applicationContext.getBean(selectedIdGenerator, IdGenerator.class);
        return new Lending(book, readerDetails, seq, lendingDuration, fineValuePerDayInCents, idGenerator.generateId());
    }

    public Lending createBootstrappingLending(Book book, ReaderDetails readerDetails, int year, int seq, LocalDate startDate, LocalDate returnedDate, int lendingDuration, int fineValuePerDayInCents) {
        IdGenerator idGenerator = applicationContext.getBean(selectedIdGenerator, IdGenerator.class);
        String lendingId = idGenerator.generateId();  // gera o lendingId

        return Lending.newBootstrappingLending(book, readerDetails, year, seq, startDate, returnedDate, lendingDuration, fineValuePerDayInCents, lendingId);
    }






}

