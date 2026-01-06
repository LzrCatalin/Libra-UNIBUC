package ro.unibuc.libra.librarymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.service.api.FineService;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;

import java.math.BigDecimal;
import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.FINES;

@RestController
@RequestMapping(FINES)
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping
    public ResponseEntity<FineDTO> createFine(@RequestBody FineDTO fineDTO) {
        var response = fineService.createFine(fineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FineDTO>> getAllFines() {
        var response = fineService.getAllFines();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineDTO> getFineById(@PathVariable("id") Long id) {
        var response = fineService.findFineById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FineDTO> updateFine(@PathVariable("id") Long id, @RequestBody FineDTO fineDTO) {
        var response = fineService.updateFine(id, fineDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FineDTO> deleteFine(@PathVariable("id") Long id) {
        var response = fineService.deleteFine(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<FineDTO> payFine(@PathVariable("id") Long id) {
        var response = fineService.payFine(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/waive")
    public ResponseEntity<FineDTO> waiveFine(@PathVariable("id") Long id) {
        var response = fineService.waiveFine(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-for-loan/{loanId}")
    public ResponseEntity<FineDTO> generateFineForLoan(@PathVariable("loanId") Long loanId) {
        var response = fineService.generateFineForLoan(loanId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/generate-overdue")
    public ResponseEntity<List<FineDTO>> generateOverdueFines() {
        var response = fineService.generateOverdueFines();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FineDTO>> getFinesByStatus(@PathVariable("status") FineStatus status) {
        var response = fineService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<FineDTO>> getFinesByMember(@PathVariable("memberId") Long memberId) {
        var response = fineService.findByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<FineDTO> getFineByLoan(@PathVariable("loanId") Long loanId) {
        var response = fineService.findByLoanId(loanId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}/total-unpaid")
    public ResponseEntity<BigDecimal> getTotalUnpaidByMember(@PathVariable("memberId") Long memberId) {
        var response = fineService.getTotalUnpaidByMember(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}/total-paid")
    public ResponseEntity<BigDecimal> getTotalPaidByMember(@PathVariable("memberId") Long memberId) {
        var response = fineService.getTotalPaidByMember(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-collected")
    public ResponseEntity<BigDecimal> getTotalCollected() {
        var response = fineService.getTotalCollected();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstanding() {
        var response = fineService.getTotalOutstanding();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<FineDTO>> getUnpaidFines() {
        var response = fineService.findByStatus(FineStatus.PENDING);
        return ResponseEntity.ok(response);
    }
}
