package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "lendings")
public class MongoLending {

    @Id
    private String id;
    private String lendingNumber;
    private String bookId;          
    private String readerDetailsId;  
    private LocalDate startDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;
    private String commentary;
    private int fineValuePerDayInCents;

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLendingNumber() {
        return lendingNumber;
    }

    public void setLendingNumber(String lendingNumber) {
        this.lendingNumber = lendingNumber;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getReaderDetailsId() {
        return readerDetailsId;
    }

    public void setReaderDetailsId(String readerDetailsId) {
        this.readerDetailsId = readerDetailsId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getFineValuePerDayInCents() {
        return fineValuePerDayInCents;
    }

    public void setFineValuePerDayInCents(int fineValuePerDayInCents) {
        this.fineValuePerDayInCents = fineValuePerDayInCents;
    }
}
