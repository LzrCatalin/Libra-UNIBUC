package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Loan;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l " +
            "LEFT JOIN FETCH l.member " +
            "LEFT JOIN FETCH l.book " +
            "WHERE l.id = :id")
    Optional<Loan> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT l FROM Loan l " +
            "LEFT JOIN FETCH l.member " +
            "LEFT JOIN FETCH l.book")
    List<Loan> findAllWithDetails();

    @Query("SELECT l FROM Loan l " +
            "LEFT JOIN FETCH l.member " +
            "LEFT JOIN FETCH l.book " +
            "WHERE l.member.id = :memberId")
    List<Loan> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT l FROM Loan l " +
            "LEFT JOIN FETCH l.member " +
            "LEFT JOIN FETCH l.book " +
            "WHERE l.book.id = :bookId")
    List<Loan> findByBookId(@Param("bookId") Long bookId);

    @Query("SELECT l FROM Loan l " +
            "LEFT JOIN FETCH l.member " +
            "LEFT JOIN FETCH l.book " +
            "WHERE l.status = :status")
    List<Loan> findByStatus(@Param("status") LoanStatus status);

    @Query("SELECT COUNT(l) FROM Loan l " +
            "WHERE l.member.id = :memberId AND l.status = :status")
    Long countByMemberIdAndStatus(@Param("memberId") Long memberId,
                                   @Param("status") LoanStatus status);

    @Query("SELECT l FROM Loan l " +
            "LEFT JOIN FETCH l.member " +
            "LEFT JOIN FETCH l.book " +
            "WHERE l.dueDate < :date AND l.status = :status")
    List<Loan> findOverdueLoans(@Param("date") ZonedDateTime date,
                                 @Param("status") LoanStatus status);
}
