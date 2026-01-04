package ro.unibuc.libra.librarymanagement.dto;

import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class FineDTO {

    private Long id;
    private BigDecimal amount;
    private String reason;
    private ZonedDateTime issuedDate;
    private ZonedDateTime paidDate;
    private FineStatus status;
    private LoanDTO loan;
    private MemberDTO member;

    public FineDTO() {
    }

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

    public LoanDTO getLoan() {
        return loan;
    }

    public void setLoan(LoanDTO loan) {
        this.loan = loan;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "FineDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", reason='" + reason + '\'' +
                ", issuedDate=" + issuedDate +
                ", paidDate=" + paidDate +
                ", status=" + status +
                ", loan=" + loan +
                ", member=" + member +
                '}';
    }
}
