package ro.unibuc.libra.librarymanagement.dto;

import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;

public class LoanDTO {

    private Long id;
    private ZonedDateTime loanDate;
    private ZonedDateTime dueDate;
    private ZonedDateTime returnDate;
    private LoanStatus status;
    private MemberDTO member;
    private BookDTO book;
    private FineDTO fine;

    public LoanDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(ZonedDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public ZonedDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public FineDTO getFine() {
        return fine;
    }

    public void setFine(FineDTO fine) {
        this.fine = fine;
    }

    @Override
    public String toString() {
        return "LoanDTO{" +
                "id=" + id +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                ", member=" + member +
                ", book=" + book +
                ", fine=" + fine +
                '}';
    }
}
