package ro.unibuc.libra.librarymanagement.dto;

import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.time.ZonedDateTime;

public class ReservationDTO {

    private Long id;
    private ZonedDateTime reservationDate;
    private ZonedDateTime expiryDate;
    private ReservationStatus status;
    private Boolean notificationSent;
    private MemberDTO member;
    private BookDTO book;

    public ReservationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(ZonedDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(ZonedDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Boolean getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(Boolean notificationSent) {
        this.notificationSent = notificationSent;
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

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "id=" + id +
                ", reservationDate=" + reservationDate +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                ", notificationSent=" + notificationSent +
                ", member=" + member +
                ", book=" + book +
                '}';
    }
}
