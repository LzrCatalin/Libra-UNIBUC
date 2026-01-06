package ro.unibuc.libra.librarymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.service.api.LoanService;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.time.ZonedDateTime;
import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.LOANS;

@RestController
@RequestMapping(LOANS)
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO) {
        var response = loanService.createLoan(loanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        var response = loanService.getAllLoans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable("id") Long id) {
        var response = loanService.findLoanById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable("id") Long id, @RequestBody LoanDTO loanDTO) {
        var response = loanService.updateLoan(id, loanDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoanDTO> deleteLoan(@PathVariable("id") Long id) {
        var response = loanService.deleteLoan(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable("id") Long id) {
        var response = loanService.returnLoan(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<LoanDTO> renewLoan(@PathVariable("id") Long id) {
        var response = loanService.renewLoan(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanDTO>> getLoansByStatus(@PathVariable("status") LoanStatus status) {
        var response = loanService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanDTO>> getLoansByMember(@PathVariable("memberId") Long memberId) {
        var response = loanService.findByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanDTO>> getLoansByBook(@PathVariable("bookId") Long bookId) {
        var response = loanService.findByBookId(bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}/active-count")
    public ResponseEntity<Long> getActiveLoanCountByMember(@PathVariable("memberId") Long memberId) {
        var response = loanService.countActiveLoansByMember(memberId);
        return ResponseEntity.ok(response);
    }
}
