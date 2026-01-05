package ro.unibuc.libra.librarymanagement.controller;

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

@RestController
@RequestMapping(MEMBERS)
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        var response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> fetchMember(@PathVariable("id") Long id) {
        var response = memberService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@RequestBody MemberDTO memberDTO) {
        var response = memberService.createMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO) {
        var response = memberService.updateMember(id, memberDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberDTO> deleteMember(@PathVariable("id") Long id) {
        var response = memberService.deleteMember(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<MemberDTO>> searchByName(@RequestParam("name") String name) {
        var response = memberService.searchByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/email")
    public ResponseEntity<List<MemberDTO>> searchByEmail(@RequestParam("email") String email) {
        var response = memberService.searchByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/membership-type/{type}")
    public ResponseEntity<List<MemberDTO>> findByMembershipType(@PathVariable("type") MembershipType type) {
        var response = memberService.findByMembershipType(type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MemberDTO>> findByStatus(@PathVariable("status") MemberStatus status) {
        var response = memberService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/loans")
    public ResponseEntity<List<LoanDTO>> getMemberLoans(@PathVariable("id") Long id) {
        var response = memberService.getMemberLoans(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDTO>> getMemberReservations(@PathVariable("id") Long id) {
        var response = memberService.getMemberReservations(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/fines")
    public ResponseEntity<List<FineDTO>> getMemberFines(@PathVariable("id") Long id) {
        var response = memberService.getMemberFines(id);
        return ResponseEntity.ok(response);
    }
}
