package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Fine;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("SELECT f FROM Fine f " +
            "LEFT JOIN FETCH f.member " +
            "LEFT JOIN FETCH f.loan " +
            "WHERE f.id = :id")
    Optional<Fine> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT f FROM Fine f " +
            "LEFT JOIN FETCH f.member " +
            "LEFT JOIN FETCH f.loan")
    List<Fine> findAllWithDetails();

    @Query("SELECT f FROM Fine f " +
            "LEFT JOIN FETCH f.member " +
            "LEFT JOIN FETCH f.loan " +
            "WHERE f.member.id = :memberId")
    List<Fine> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT f FROM Fine f " +
            "LEFT JOIN FETCH f.member " +
            "LEFT JOIN FETCH f.loan " +
            "WHERE f.loan.id = :loanId")
    Optional<Fine> findByLoanId(@Param("loanId") Long loanId);

    @Query("SELECT f FROM Fine f " +
            "LEFT JOIN FETCH f.member " +
            "LEFT JOIN FETCH f.loan " +
            "WHERE f.status = :status")
    List<Fine> findByStatus(@Param("status") FineStatus status);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f " +
            "WHERE f.member.id = :memberId AND f.status = :status")
    BigDecimal sumAmountByMemberIdAndStatus(@Param("memberId") Long memberId,
                                            @Param("status") FineStatus status);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f " +
            "WHERE f.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") FineStatus status);

    boolean existsByLoanId(Long loanId);
}
