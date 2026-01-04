package ro.unibuc.libra.librarymanagement.entity;

import jakarta.persistence.*;
import ro.unibuc.libra.librarymanagement.config.GenericID;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;

@Entity
@Table(name = "loan")
public class Loan implements GenericID<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "loan_date")
    private ZonedDateTime loanDate;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne(mappedBy = "loan", fetch = FetchType.LAZY)
    private Fine fine;

    public Loan() {}

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

    public Fine getFine() {
        return fine;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
    }
}
