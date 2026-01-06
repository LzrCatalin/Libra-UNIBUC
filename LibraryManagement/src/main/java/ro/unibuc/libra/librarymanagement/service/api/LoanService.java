package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;
import java.util.List;

public interface LoanService {

    LoanDTO createLoan(LoanDTO loanDTO);
    List<LoanDTO> getAllLoans();
    LoanDTO findLoanById(Long id);
    LoanDTO updateLoan(Long id, LoanDTO loanDTO);
    LoanDTO deleteLoan(Long id);

    LoanDTO returnLoan(Long id);
    LoanDTO renewLoan(Long id);

    List<LoanDTO> findByStatus(LoanStatus status);
    List<LoanDTO> findByMemberId(Long memberId);
    List<LoanDTO> findByBookId(Long bookId);

    Long countActiveLoansByMember(Long memberId);
}
