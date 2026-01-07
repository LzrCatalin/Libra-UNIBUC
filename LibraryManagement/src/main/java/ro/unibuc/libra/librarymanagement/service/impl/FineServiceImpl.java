package ro.unibuc.libra.librarymanagement.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.entity.Fine;
import ro.unibuc.libra.librarymanagement.entity.Loan;
import ro.unibuc.libra.librarymanagement.mapper.FineMapper;
import ro.unibuc.libra.librarymanagement.repository.FineRepository;
import ro.unibuc.libra.librarymanagement.repository.LoanRepository;
import ro.unibuc.libra.librarymanagement.service.api.FineService;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class FineServiceImpl implements FineService {

    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("0.50");

    private final FineRepository fineRepository;
    private final LoanRepository loanRepository;
    private final FineMapper fineMapper;

    public FineServiceImpl(FineRepository fineRepository,
                           LoanRepository loanRepository,
                           FineMapper fineMapper) {
        this.fineRepository = fineRepository;
        this.loanRepository = loanRepository;
        this.fineMapper = fineMapper;
    }

    @Override
    public FineDTO createFine(FineDTO fineDTO) {
        if (fineDTO.getLoan() == null || fineDTO.getLoan().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan id is required");
        }

        Loan loan = loanRepository.findByIdWithDetails(fineDTO.getLoan().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found with id: " + fineDTO.getLoan().getId()
                ));

        if (fineRepository.existsByLoanId(loan.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Fine already exists for this loan");
        }

        Fine fine = new Fine();
        fine.setLoan(loan);
        fine.setMember(loan.getMember());
        fine.setAmount(fineDTO.getAmount());
        fine.setReason(fineDTO.getReason());
        fine.setIssuedDate(ZonedDateTime.now());
        fine.setStatus(FineStatus.PENDING);

        return fineMapper.toDTO(fineRepository.save(fine));
    }

    @Override
    public List<FineDTO> getAllFines() {
        return fineRepository.findAllWithDetails()
                .stream()
                .map(fineMapper::toDTO)
                .toList();
    }

    @Override
    public FineDTO findFineById(Long id) {
        return fineMapper.toDTO(
                fineRepository.findByIdWithDetails(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Fine not found with id: " + id
                        ))
        );
    }

    @Override
    public FineDTO updateFine(Long id, FineDTO fineDTO) {
        Fine existingFine = fineRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fine not found with id: " + id
                ));

        existingFine.setAmount(fineDTO.getAmount());
        existingFine.setReason(fineDTO.getReason());
        existingFine.setIssuedDate(fineDTO.getIssuedDate());
        existingFine.setPaidDate(fineDTO.getPaidDate());
        existingFine.setStatus(fineDTO.getStatus());

        return fineMapper.toDTO(fineRepository.save(existingFine));
    }

    @Override
    public FineDTO deleteFine(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fine not found with id: " + id
                ));

        FineDTO fineDTO = fineMapper.toDTO(fine);
        fineRepository.deleteById(id);
        return fineDTO;
    }

    @Override
    public FineDTO payFine(Long id) {
        Fine fine = fineRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fine not found with id: " + id
                ));

        if (fine.getStatus() != FineStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending fines can be paid");
        }

        fine.setPaidDate(ZonedDateTime.now());
        fine.setStatus(FineStatus.PAID);

        return fineMapper.toDTO(fineRepository.save(fine));
    }

    @Override
    public FineDTO waiveFine(Long id) {
        Fine fine = fineRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fine not found with id: " + id
                ));

        if (fine.getStatus() != FineStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending fines can be waived");
        }

        fine.setStatus(FineStatus.WAIVED);

        return fineMapper.toDTO(fineRepository.save(fine));
    }

    @Override
    public FineDTO generateFineForLoan(Long loanId) {
        Loan loan = loanRepository.findByIdWithDetails(loanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found with id: " + loanId
                ));

        if (fineRepository.existsByLoanId(loanId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Fine already exists for this loan");
        }

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only active loans can have fines generated");
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (!loan.getDueDate().isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Loan is not overdue yet");
        }

        long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), now);
        BigDecimal fineAmount = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(daysOverdue));

        Fine fine = new Fine();
        fine.setLoan(loan);
        fine.setMember(loan.getMember());
        fine.setAmount(fineAmount);
        fine.setReason("Overdue loan - " + daysOverdue + " days late");
        fine.setIssuedDate(now);
        fine.setStatus(FineStatus.PENDING);

        return fineMapper.toDTO(fineRepository.save(fine));
    }

    @Override
    public List<FineDTO> generateOverdueFines() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(ZonedDateTime.now(), LoanStatus.ACTIVE);
        List<FineDTO> generatedFines = new ArrayList<>();

        ZonedDateTime now = ZonedDateTime.now();

        for (Loan loan : overdueLoans) {
            if (!fineRepository.existsByLoanId(loan.getId())) {
                long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), now);
                BigDecimal fineAmount = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(daysOverdue));

                Fine fine = new Fine();
                fine.setLoan(loan);
                fine.setMember(loan.getMember());
                fine.setAmount(fineAmount);
                fine.setReason("Overdue loan - " + daysOverdue + " days late");
                fine.setIssuedDate(now);
                fine.setStatus(FineStatus.PENDING);

                generatedFines.add(fineMapper.toDTO(fineRepository.save(fine)));
            }
        }

        return generatedFines;
    }

    @Override
    public List<FineDTO> findByStatus(FineStatus status) {
        return fineRepository.findByStatus(status)
                .stream()
                .map(fineMapper::toDTO)
                .toList();
    }

    @Override
    public List<FineDTO> findByMemberId(Long memberId) {
        return fineRepository.findByMemberId(memberId)
                .stream()
                .map(fineMapper::toDTO)
                .toList();
    }

    @Override
    public FineDTO findByLoanId(Long loanId) {
        return fineRepository.findByLoanId(loanId)
                .map(fineMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fine not found for loan id: " + loanId
                ));
    }

    @Override
    public BigDecimal getTotalUnpaidByMember(Long memberId) {
        return fineRepository.sumAmountByMemberIdAndStatus(memberId, FineStatus.PENDING);
    }

    @Override
    public BigDecimal getTotalPaidByMember(Long memberId) {
        return fineRepository.sumAmountByMemberIdAndStatus(memberId, FineStatus.PAID);
    }

    @Override
    public BigDecimal getTotalCollected() {
        return fineRepository.sumAmountByStatus(FineStatus.PAID);
    }

    @Override
    public BigDecimal getTotalOutstanding() {
        return fineRepository.sumAmountByStatus(FineStatus.PENDING);
    }
}
