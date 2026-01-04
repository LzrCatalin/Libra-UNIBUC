package ro.unibuc.libra.librarymanagement.entity;

import jakarta.persistence.*;
import ro.unibuc.libra.librarymanagement.config.GenericID;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "fine")
public class Fine implements GenericID<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "issued_date")
    private ZonedDateTime issuedDate;

    @Column(name = "paid_date")
    private ZonedDateTime paidDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FineStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", unique = true, nullable = false)
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Fine () {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(ZonedDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public ZonedDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(ZonedDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
