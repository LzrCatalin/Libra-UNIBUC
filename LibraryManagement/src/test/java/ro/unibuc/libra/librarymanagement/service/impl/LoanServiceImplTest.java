package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.Loan;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.mapper.LoanMapper;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.repository.LoanRepository;
import ro.unibuc.libra.librarymanagement.repository.MemberRepository;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Loan loan;
    private LoanDTO loanDTO;
    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAvailableCopies(5);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");

        member = new Member();
        member.setId(1L);
        member.setFirstName("John");
        member.setLastName("Doe");

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);

        loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(ZonedDateTime.now());
        loan.setDueDate(ZonedDateTime.now().plusDays(14));
        loan.setStatus(LoanStatus.ACTIVE);

        loanDTO = new LoanDTO();
        loanDTO.setId(1L);
        loanDTO.setBook(bookDTO);
        loanDTO.setMember(memberDTO);
        loanDTO.setLoanDate(ZonedDateTime.now());
        loanDTO.setDueDate(ZonedDateTime.now().plusDays(14));
        loanDTO.setStatus(LoanStatus.ACTIVE);
    }

    @Test
    void createLoan_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(bookRepository.save(book)).thenReturn(book);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        LoanDTO result = loanService.createLoan(loanDTO);

        assertNotNull(result);
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
        verify(bookRepository).findById(1L);
        verify(memberRepository).findById(1L);
        verify(loanRepository).save(any(Loan.class));
        verify(bookRepository).save(book);
        assertEquals(4, book.getAvailableCopies());
    }

    @Test
    void createLoan_BookNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.createLoan(loanDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_MemberNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.createLoan(loanDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoan_BookNotAvailable_ThrowsException() {
        book.setAvailableCopies(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThrows(IllegalStateException.class, () -> loanService.createLoan(loanDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void getAllLoans_Success() {
        List<Loan> loans = List.of(loan);
        when(loanRepository.findAllWithDetails()).thenReturn(loans);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        List<LoanDTO> result = loanService.getAllLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanRepository).findAllWithDetails();
    }

    @Test
    void getAllLoans_EmptyList() {
        when(loanRepository.findAllWithDetails()).thenReturn(Collections.emptyList());

        List<LoanDTO> result = loanService.getAllLoans();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findLoanById_Success() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        LoanDTO result = loanService.findLoanById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(loanRepository).findByIdWithDetails(1L);
    }

    @Test
    void findLoanById_NotFound_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.findLoanById(1L));
    }

    @Test
    void updateLoan_Success() {
        LoanDTO updateDTO = new LoanDTO();
        updateDTO.setLoanDate(ZonedDateTime.now());
        updateDTO.setDueDate(ZonedDateTime.now().plusDays(20));
        updateDTO.setReturnDate(ZonedDateTime.now());
        updateDTO.setStatus(LoanStatus.RETURNED);

        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanMapper.toDTO(loan)).thenReturn(updateDTO);

        LoanDTO result = loanService.updateLoan(1L, updateDTO);

        assertNotNull(result);
        verify(loanRepository).findByIdWithDetails(1L);
        verify(loanRepository).save(loan);
    }

    @Test
    void updateLoan_NotFound_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.updateLoan(1L, loanDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void deleteLoan_Success() {
        when(loanRepository.existsById(1L)).thenReturn(true);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);
        doNothing().when(loanRepository).deleteById(1L);

        LoanDTO result = loanService.deleteLoan(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(loanRepository).deleteById(1L);
    }

    @Test
    void deleteLoan_NotFound_ThrowsException() {
        when(loanRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> loanService.deleteLoan(1L));
        verify(loanRepository, never()).deleteById(anyLong());
    }

    @Test
    void returnLoan_Success() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);
        when(bookRepository.save(book)).thenReturn(book);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        int originalCopies = book.getAvailableCopies();

        LoanDTO result = loanService.returnLoan(1L);

        assertNotNull(result);
        assertEquals(originalCopies + 1, book.getAvailableCopies());
        verify(loanRepository).findByIdWithDetails(1L);
        verify(loanRepository).save(loan);
        verify(bookRepository).save(book);
    }

    @Test
    void returnLoan_AlreadyReturned_ThrowsException() {
        loan.setStatus(LoanStatus.RETURNED);
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));

        assertThrows(IllegalStateException.class, () -> loanService.returnLoan(1L));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void returnLoan_NotFound_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.returnLoan(1L));
    }

    @Test
    void renewLoan_Success() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        ZonedDateTime originalDueDate = loan.getDueDate();

        LoanDTO result = loanService.renewLoan(1L);

        assertNotNull(result);
        verify(loanRepository).findByIdWithDetails(1L);
        verify(loanRepository).save(loan);
    }

    @Test
    void renewLoan_NotActive_ThrowsException() {
        loan.setStatus(LoanStatus.RETURNED);
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(loan));

        assertThrows(IllegalStateException.class, () -> loanService.renewLoan(1L));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void renewLoan_NotFound_ThrowsException() {
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.renewLoan(1L));
    }

    @Test
    void findByStatus_Success() {
        List<Loan> loans = List.of(loan);
        when(loanRepository.findByStatus(LoanStatus.ACTIVE)).thenReturn(loans);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        List<LoanDTO> result = loanService.findByStatus(LoanStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanRepository).findByStatus(LoanStatus.ACTIVE);
    }

    @Test
    void findByMemberId_Success() {
        List<Loan> loans = List.of(loan);
        when(loanRepository.findByMemberId(1L)).thenReturn(loans);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        List<LoanDTO> result = loanService.findByMemberId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanRepository).findByMemberId(1L);
    }

    @Test
    void findByBookId_Success() {
        List<Loan> loans = List.of(loan);
        when(loanRepository.findByBookId(1L)).thenReturn(loans);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        List<LoanDTO> result = loanService.findByBookId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanRepository).findByBookId(1L);
    }

    @Test
    void countActiveLoansByMember_Success() {
        when(loanRepository.countByMemberIdAndStatus(1L, LoanStatus.ACTIVE)).thenReturn(3L);

        Long result = loanService.countActiveLoansByMember(1L);

        assertEquals(3L, result);
        verify(loanRepository).countByMemberIdAndStatus(1L, LoanStatus.ACTIVE);
    }
}
