package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Reservation;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.member " +
            "LEFT JOIN FETCH r.book " +
            "WHERE r.id = :id")
    Optional<Reservation> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM Reservation r " +
            "LEFT JOIN FETCH r.member " +
            "LEFT JOIN FETCH r.book")
    List<Reservation> findAllWithDetails();

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.member " +
            "LEFT JOIN FETCH r.book " +
            "WHERE r.member.id = :memberId")
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.member " +
            "LEFT JOIN FETCH r.book " +
            "WHERE r.book.id = :bookId")
    List<Reservation> findByBookId(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.member " +
            "LEFT JOIN FETCH r.book " +
            "WHERE r.status = :status")
    List<Reservation> findByStatus(@Param("status") ReservationStatus status);

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.member.id = :memberId AND r.status = :status")
    Long countByMemberIdAndStatus(@Param("memberId") Long memberId,
                                   @Param("status") ReservationStatus status);

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.member " +
            "LEFT JOIN FETCH r.book " +
            "WHERE r.expiryDate < :date AND r.status = :status")
    List<Reservation> findExpiredReservations(@Param("date") ZonedDateTime date,
                                               @Param("status") ReservationStatus status);
}
