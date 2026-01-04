package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
