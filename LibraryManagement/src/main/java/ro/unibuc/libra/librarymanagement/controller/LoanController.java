package ro.unibuc.libra.librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.service.api.LoanService;
import ro.unibuc.libra.librarymanagement.util.enums.LoanStatus;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.LOANS;

@Tag(name = "Loans")
@RestController
@RequestMapping(LOANS)
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Create loan")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO) {
        var response = loanService.createLoan(loanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all loans")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        var response = loanService.getAllLoans();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get loan by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable("id") Long id) {
        var response = loanService.findLoanById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update loan")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable("id") Long id, @RequestBody LoanDTO loanDTO) {
        var response = loanService.updateLoan(id, loanDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete loan")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<LoanDTO> deleteLoan(@PathVariable("id") Long id) {
        var response = loanService.deleteLoan(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Return loan")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable("id") Long id) {
        var response = loanService.returnLoan(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Renew loan")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/renew")
    public ResponseEntity<LoanDTO> renewLoan(@PathVariable("id") Long id) {
        var response = loanService.renewLoan(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get loans by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanDTO>> getLoansByStatus(@PathVariable("status") LoanStatus status) {
        var response = loanService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get loans by member id")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanDTO>> getLoansByMember(@PathVariable("memberId") Long memberId) {
        var response = loanService.findByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get loans by book id")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanDTO>> getLoansByBook(@PathVariable("bookId") Long bookId) {
        var response = loanService.findByBookId(bookId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get active loan count by member")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}/active-count")
    public ResponseEntity<Long> getActiveLoanCountByMember(@PathVariable("memberId") Long memberId) {
        var response = loanService.countActiveLoansByMember(memberId);
        return ResponseEntity.ok(response);
    }
}
