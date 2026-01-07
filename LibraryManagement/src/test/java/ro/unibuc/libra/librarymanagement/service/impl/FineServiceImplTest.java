package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.entity.Fine;
import ro.unibuc.libra.librarymanagement.entity.Loan;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.mapper.FineMapper;
import ro.unibuc.libra.librarymanagement.repository.FineRepository;
import ro.unibuc.libra.librarymanagement.repository.LoanRepository;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineServiceImplTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private FineMapper fineMapper;

    @InjectMocks
    private FineServiceImpl fineService;

    private Fine fine;
    private FineDTO fineDTO;
    private Loan loan;

    @BeforeEach
    void setUp() {
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("John");
        member.setLastName("Doe");

        loan = new Loan();
        loan.setId(1L);
        loan.setMember(member);
        loan.setLoanDate(ZonedDateTime.now().minusDays(20));
        loan.setDueDate(ZonedDateTime.now().minusDays(5));
        loan.setStatus(LoanStatus.ACTIVE);

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setId(1L);

        fine = new Fine();
        fine.setId(1L);
        fine.setLoan(loan);
        fine.setMember(member);
        fine.setAmount(new BigDecimal("10.50"));
        fine.setReason("Overdue loan");
        fine.setIssuedDate(ZonedDateTime.now());
        fine.setStatus(FineStatus.PENDING);

        fineDTO = new FineDTO();
        fineDTO.setId(1L);
        fineDTO.setLoan(loanDTO);
        fineDTO.setAmount(new BigDecimal("10.50"));
        fineDTO.setReason("Overdue loan");
        fineDTO.setIssuedDate(ZonedDateTime.now());
        fineDTO.setStatus(FineStatus.PENDING);
    }

    @Test
    void createFine_Success() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoanId(1L)).thenReturn(false);
        when(fineRepository.save(any(Fine.class))).thenReturn(fine);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        FineDTO result = fineService.createFine(fineDTO);

        assertNotNull(result);
        assertEquals(FineStatus.PENDING, result.getStatus());
        verify(loanRepository).findByIdWithDetails(1L);
        verify(fineRepository).existsByLoanId(1L);
        verify(fineRepository).save(any(Fine.class));
    }

    @Test
    void createFine_LoanNotFound_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fineService.createFine(fineDTO));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void createFine_FineAlreadyExists_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoanId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> fineService.createFine(fineDTO));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void getAllFines_Success() {
        List<Fine> fines = List.of(fine);
        when(fineRepository.findAllWithDetails()).thenReturn(fines);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        List<FineDTO> result = fineService.getAllFines();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(fineRepository).findAllWithDetails();
    }

    @Test
    void getAllFines_EmptyList() {
        when(fineRepository.findAllWithDetails()).thenReturn(Collections.emptyList());

        List<FineDTO> result = fineService.getAllFines();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findFineById_Success() {
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(fine));
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        FineDTO result = fineService.findFineById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(fineRepository).findByIdWithDetails(1L);
    }

    @Test
    void findFineById_NotFound_ThrowsException() {
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fineService.findFineById(1L));
    }

    @Test
    void updateFine_Success() {
        FineDTO updateDTO = new FineDTO();
        updateDTO.setAmount(new BigDecimal("15.00"));
        updateDTO.setReason("Updated reason");
        updateDTO.setIssuedDate(ZonedDateTime.now());
        updateDTO.setPaidDate(ZonedDateTime.now());
        updateDTO.setStatus(FineStatus.PAID);

        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.save(fine)).thenReturn(fine);
        when(fineMapper.toDTO(fine)).thenReturn(updateDTO);

        FineDTO result = fineService.updateFine(1L, updateDTO);

        assertNotNull(result);
        verify(fineRepository).findByIdWithDetails(1L);
        verify(fineRepository).save(fine);
    }

    @Test
    void updateFine_NotFound_ThrowsException() {
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fineService.updateFine(1L, fineDTO));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void deleteFine_Success() {
        when(fineRepository.existsById(1L)).thenReturn(true);
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);
        doNothing().when(fineRepository).deleteById(1L);

        FineDTO result = fineService.deleteFine(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(fineRepository).deleteById(1L);
    }

    @Test
    void deleteFine_NotFound_ThrowsException() {
        when(fineRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> fineService.deleteFine(1L));
        verify(fineRepository, never()).deleteById(anyLong());
    }

    @Test
    void payFine_Success() {
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.save(fine)).thenReturn(fine);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        FineDTO result = fineService.payFine(1L);

        assertNotNull(result);
        verify(fineRepository).findByIdWithDetails(1L);
        verify(fineRepository).save(fine);
    }

    @Test
    void payFine_NotPending_ThrowsException() {
        fine.setStatus(FineStatus.PAID);
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(fine));

        assertThrows(IllegalStateException.class, () -> fineService.payFine(1L));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void payFine_NotFound_ThrowsException() {
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fineService.payFine(1L));
    }

    @Test
    void waiveFine_Success() {
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.save(fine)).thenReturn(fine);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        FineDTO result = fineService.waiveFine(1L);

        assertNotNull(result);
        verify(fineRepository).findByIdWithDetails(1L);
        verify(fineRepository).save(fine);
    }

    @Test
    void waiveFine_NotPending_ThrowsException() {
        fine.setStatus(FineStatus.WAIVED);
        when(fineRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(fine));

        assertThrows(IllegalStateException.class, () -> fineService.waiveFine(1L));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void generateFineForLoan_Success() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoanId(1L)).thenReturn(false);
        when(fineRepository.save(any(Fine.class))).thenReturn(fine);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        FineDTO result = fineService.generateFineForLoan(1L);

        assertNotNull(result);
        assertEquals(FineStatus.PENDING, result.getStatus());
        verify(loanRepository).findByIdWithDetails(1L);
        verify(fineRepository).save(any(Fine.class));
    }

    @Test
    void generateFineForLoan_LoanNotFound_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fineService.generateFineForLoan(1L));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void generateFineForLoan_FineAlreadyExists_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoanId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> fineService.generateFineForLoan(1L));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void generateFineForLoan_LoanNotActive_ThrowsException() {
        loan.setStatus(LoanStatus.RETURNED);
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoanId(1L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> fineService.generateFineForLoan(1L));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void generateFineForLoan_LoanNotOverdue_ThrowsException() {
        loan.setDueDate(ZonedDateTime.now().plusDays(5));
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoanId(1L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> fineService.generateFineForLoan(1L));
        verify(fineRepository, never()).save(any());
    }

    @Test
    void generateOverdueFines_Success() {
        List<Loan> overdueLoans = List.of(loan);
        when(loanRepository.findOverdueLoans(any(ZonedDateTime.class), eq(LoanStatus.ACTIVE)))
                .thenReturn(overdueLoans);
        when(fineRepository.existsByLoanId(1L)).thenReturn(false);
        when(fineRepository.save(any(Fine.class))).thenReturn(fine);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        List<FineDTO> result = fineService.generateOverdueFines();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanRepository).findOverdueLoans(any(ZonedDateTime.class), eq(LoanStatus.ACTIVE));
        verify(fineRepository).save(any(Fine.class));
    }

    @Test
    void generateOverdueFines_SkipExistingFines() {
        List<Loan> overdueLoans = List.of(loan);
        when(loanRepository.findOverdueLoans(any(ZonedDateTime.class), eq(LoanStatus.ACTIVE)))
                .thenReturn(overdueLoans);
        when(fineRepository.existsByLoanId(1L)).thenReturn(true);

        List<FineDTO> result = fineService.generateOverdueFines();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fineRepository, never()).save(any());
    }

    @Test
    void findByStatus_Success() {
        List<Fine> fines = List.of(fine);
        when(fineRepository.findByStatus(FineStatus.PENDING)).thenReturn(fines);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        List<FineDTO> result = fineService.findByStatus(FineStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(fineRepository).findByStatus(FineStatus.PENDING);
    }

    @Test
    void findByMemberId_Success() {
        List<Fine> fines = List.of(fine);
        when(fineRepository.findByMemberId(1L)).thenReturn(fines);
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        List<FineDTO> result = fineService.findByMemberId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(fineRepository).findByMemberId(1L);
    }

    @Test
    void findByLoanId_Success() {
        when(fineRepository.findByLoanId(1L)).thenReturn(Optional.of(fine));
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        FineDTO result = fineService.findByLoanId(1L);

        assertNotNull(result);
        verify(fineRepository).findByLoanId(1L);
    }

    @Test
    void findByLoanId_NotFound_ThrowsException() {
        when(fineRepository.findByLoanId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fineService.findByLoanId(1L));
    }

    @Test
    void getTotalUnpaidByMember_Success() {
        BigDecimal total = new BigDecimal("25.50");
        when(fineRepository.sumAmountByMemberIdAndStatus(1L, FineStatus.PENDING)).thenReturn(total);

        BigDecimal result = fineService.getTotalUnpaidByMember(1L);

        assertEquals(total, result);
        verify(fineRepository).sumAmountByMemberIdAndStatus(1L, FineStatus.PENDING);
    }

    @Test
    void getTotalPaidByMember_Success() {
        BigDecimal total = new BigDecimal("50.00");
        when(fineRepository.sumAmountByMemberIdAndStatus(1L, FineStatus.PAID)).thenReturn(total);

        BigDecimal result = fineService.getTotalPaidByMember(1L);

        assertEquals(total, result);
        verify(fineRepository).sumAmountByMemberIdAndStatus(1L, FineStatus.PAID);
    }

    @Test
    void getTotalCollected_Success() {
        BigDecimal total = new BigDecimal("1000.00");
        when(fineRepository.sumAmountByStatus(FineStatus.PAID)).thenReturn(total);

        BigDecimal result = fineService.getTotalCollected();

        assertEquals(total, result);
        verify(fineRepository).sumAmountByStatus(FineStatus.PAID);
    }

    @Test
    void getTotalOutstanding_Success() {
        BigDecimal total = new BigDecimal("500.00");
        when(fineRepository.sumAmountByStatus(FineStatus.PENDING)).thenReturn(total);

        BigDecimal result = fineService.getTotalOutstanding();

        assertEquals(total, result);
        verify(fineRepository).sumAmountByStatus(FineStatus.PENDING);
    }
}
