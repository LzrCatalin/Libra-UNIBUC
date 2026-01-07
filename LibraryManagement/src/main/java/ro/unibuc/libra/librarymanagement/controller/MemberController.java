package ro.unibuc.libra.librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.service.api.MemberService;
import ro.unibuc.libra.librarymanagement.util.enums.MemberStatus;
import ro.unibuc.libra.librarymanagement.util.enums.MembershipType;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.MEMBERS;

@Tag(name = "Members")
@RestController
@RequestMapping(MEMBERS)
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "Get all members")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        var response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get member by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> fetchMember(@PathVariable("id") Long id) {
        var response = memberService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create member")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400")
    })
    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@RequestBody MemberDTO memberDTO) {
        var response = memberService.createMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update member")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO) {
        var response = memberService.updateMember(id, memberDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete member")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MemberDTO> deleteMember(@PathVariable("id") Long id) {
        var response = memberService.deleteMember(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search members by name")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/search/name")
    public ResponseEntity<List<MemberDTO>> searchByName(@RequestParam("name") String name) {
        var response = memberService.searchByName(name);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search members by email")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/search/email")
    public ResponseEntity<List<MemberDTO>> searchByEmail(@RequestParam("email") String email) {
        var response = memberService.searchByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get members by membership type")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400")
    })
    @GetMapping("/membership-type/{type}")
    public ResponseEntity<List<MemberDTO>> findByMembershipType(@PathVariable("type") MembershipType type) {
        var response = memberService.findByMembershipType(type);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get members by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MemberDTO>> findByStatus(@PathVariable("status") MemberStatus status) {
        var response = memberService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get loans for a member")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}/loans")
    public ResponseEntity<List<LoanDTO>> getMemberLoans(@PathVariable("id") Long id) {
        var response = memberService.getMemberLoans(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reservations for a member")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDTO>> getMemberReservations(@PathVariable("id") Long id) {
        var response = memberService.getMemberReservations(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get fines for a member")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}/fines")
    public ResponseEntity<List<FineDTO>> getMemberFines(@PathVariable("id") Long id) {
        var response = memberService.getMemberFines(id);
        return ResponseEntity.ok(response);
    }
}
