package ro.unibuc.libra.librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.service.api.FineService;
import ro.unibuc.libra.librarymanagement.util.enums.FineStatus;

import java.math.BigDecimal;
import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.FINES;

@Tag(name = "Fines")
@RestController
@RequestMapping(FINES)
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @Operation(summary = "Create fine")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping
    public ResponseEntity<FineDTO> createFine(@RequestBody FineDTO fineDTO) {
        var response = fineService.createFine(fineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all fines")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping
    public ResponseEntity<List<FineDTO>> getAllFines() {
        var response = fineService.getAllFines();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get fine by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FineDTO> getFineById(@PathVariable("id") Long id) {
        var response = fineService.findFineById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update fine")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FineDTO> updateFine(@PathVariable("id") Long id, @RequestBody FineDTO fineDTO) {
        var response = fineService.updateFine(id, fineDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete fine")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<FineDTO> deleteFine(@PathVariable("id") Long id) {
        var response = fineService.deleteFine(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Pay fine")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/pay")
    public ResponseEntity<FineDTO> payFine(@PathVariable("id") Long id) {
        var response = fineService.payFine(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Waive fine")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/waive")
    public ResponseEntity<FineDTO> waiveFine(@PathVariable("id") Long id) {
        var response = fineService.waiveFine(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate fine for a loan")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/generate-for-loan/{loanId}")
    public ResponseEntity<FineDTO> generateFineForLoan(@PathVariable("loanId") Long loanId) {
        var response = fineService.generateFineForLoan(loanId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Generate fines for overdue loans")
    @ApiResponses({ @ApiResponse(responseCode = "201") })
    @PostMapping("/generate-overdue")
    public ResponseEntity<List<FineDTO>> generateOverdueFines() {
        var response = fineService.generateOverdueFines();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get fines by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FineDTO>> getFinesByStatus(@PathVariable("status") FineStatus status) {
        var response = fineService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get fines by member id")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<FineDTO>> getFinesByMember(@PathVariable("memberId") Long memberId) {
        var response = fineService.findByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get fine by loan id")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<FineDTO> getFineByLoan(@PathVariable("loanId") Long loanId) {
        var response = fineService.findByLoanId(loanId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get total unpaid fines amount for member")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}/total-unpaid")
    public ResponseEntity<BigDecimal> getTotalUnpaidByMember(@PathVariable("memberId") Long memberId) {
        var response = fineService.getTotalUnpaidByMember(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get total paid fines amount for member")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}/total-paid")
    public ResponseEntity<BigDecimal> getTotalPaidByMember(@PathVariable("memberId") Long memberId) {
        var response = fineService.getTotalPaidByMember(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get total collected fines amount")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/total-collected")
    public ResponseEntity<BigDecimal> getTotalCollected() {
        var response = fineService.getTotalCollected();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get total outstanding fines amount")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/total-outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstanding() {
        var response = fineService.getTotalOutstanding();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get unpaid fines")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/unpaid")
    public ResponseEntity<List<FineDTO>> getUnpaidFines() {
        var response = fineService.findByStatus(FineStatus.PENDING);
        return ResponseEntity.ok(response);
    }
}
