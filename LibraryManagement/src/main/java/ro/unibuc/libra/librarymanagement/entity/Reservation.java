package ro.unibuc.libra.librarymanagement.entity;

import jakarta.persistence.*;
import ro.unibuc.libra.librarymanagement.config.GenericID;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.time.ZonedDateTime;

@Entity
@Table(name = "reservation")
public class Reservation implements GenericID<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reservation_date")
    private ZonedDateTime reservationDate;

    @Column(name = "expiry_date")
    private ZonedDateTime expiryDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "notification_sent")
    private boolean notificationSent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Reservation() {}

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

    public boolean isNotificationSend() {
        return notificationSent;
    }

    public void setNotificationSend(boolean notificationSent) {
        this.notificationSent = notificationSent;
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
}
