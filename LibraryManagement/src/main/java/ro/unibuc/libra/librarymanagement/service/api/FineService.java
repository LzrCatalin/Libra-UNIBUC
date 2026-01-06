package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;

import java.math.BigDecimal;
import java.util.List;

public interface FineService {

    FineDTO createFine(FineDTO fineDTO);
    List<FineDTO> getAllFines();
    FineDTO findFineById(Long id);
    FineDTO updateFine(Long id, FineDTO fineDTO);
    FineDTO deleteFine(Long id);

    FineDTO payFine(Long id);
    FineDTO waiveFine(Long id);
    FineDTO generateFineForLoan(Long loanId);
    List<FineDTO> generateOverdueFines();

    List<FineDTO> findByStatus(FineStatus status);
    List<FineDTO> findByMemberId(Long memberId);
    FineDTO findByLoanId(Long loanId);

    BigDecimal getTotalUnpaidByMember(Long memberId);
    BigDecimal getTotalPaidByMember(Long memberId);
    BigDecimal getTotalCollected();
    BigDecimal getTotalOutstanding();
}
