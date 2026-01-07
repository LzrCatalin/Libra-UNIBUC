package ro.unibuc.libra.librarymanagement.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.Loan;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.mapper.LoanMapper;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.repository.LoanRepository;
import ro.unibuc.libra.librarymanagement.repository.MemberRepository;
import ro.unibuc.libra.librarymanagement.service.api.LoanService;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanMapper loanMapper;

    public LoanServiceImpl(LoanRepository loanRepository,
                           BookRepository bookRepository,
                           MemberRepository memberRepository,
                           LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanMapper = loanMapper;
    }

    @Override
    public LoanDTO createLoan(LoanDTO loanDTO) {
        if (loanDTO.getBook() == null || loanDTO.getBook().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book id is required");
        }
        if (loanDTO.getMember() == null || loanDTO.getMember().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member id is required");
        }

        Book book = bookRepository.findById(loanDTO.getBook().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book not found with id: " + loanDTO.getBook().getId()
                ));

        Member member = memberRepository.findById(loanDTO.getMember().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found with id: " + loanDTO.getMember().getId()
                ));

        if (book.getAvailableCopies() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is not available for loan");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(ZonedDateTime.now());
        loan.setDueDate(ZonedDateTime.now().plusDays(14));
        loan.setStatus(LoanStatus.ACTIVE);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return loanMapper.toDTO(loanRepository.save(loan));
    }

    @Override
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAllWithDetails()
                .stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    @Override
    public LoanDTO findLoanById(Long id) {
        return loanMapper.toDTO(
                loanRepository.findByIdWithDetails(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Loan not found with id: " + id
                        ))
        );
    }

    @Override
    public LoanDTO updateLoan(Long id, LoanDTO loanDTO) {
        Loan existingLoan = loanRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found with id: " + id
                ));

        existingLoan.setLoanDate(loanDTO.getLoanDate());
        existingLoan.setDueDate(loanDTO.getDueDate());
        existingLoan.setReturnDate(loanDTO.getReturnDate());
        existingLoan.setStatus(loanDTO.getStatus());

        return loanMapper.toDTO(loanRepository.save(existingLoan));
    }

    @Override
    public LoanDTO deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found with id: " + id
                ));

        LoanDTO loanDTO = loanMapper.toDTO(loan);
        loanRepository.deleteById(id);
        return loanDTO;
    }

    @Override
    public LoanDTO returnLoan(Long id) {
        Loan loan = loanRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found with id: " + id
                ));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Loan has already been returned");
        }

        loan.setReturnDate(ZonedDateTime.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanMapper.toDTO(loanRepository.save(loan));
    }

    @Override
    public LoanDTO renewLoan(Long id) {
        Loan loan = loanRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found with id: " + id
                ));

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only active loans can be renewed");
        }

        loan.setDueDate(loan.getDueDate().plusDays(14));

        return loanMapper.toDTO(loanRepository.save(loan));
    }

    @Override
    public List<LoanDTO> findByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status)
                .stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId)
                .stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId)
                .stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    @Override
    public Long countActiveLoansByMember(Long memberId) {
        return loanRepository.countByMemberIdAndStatus(memberId, LoanStatus.ACTIVE);
    }
}
