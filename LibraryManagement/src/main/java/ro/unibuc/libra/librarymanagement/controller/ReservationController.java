package ro.unibuc.libra.librarymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.service.api.ReservationService;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.RESERVATIONS;

@RestController
@RequestMapping(RESERVATIONS)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        var response = reservationService.createReservation(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        var response = reservationService.getAllReservations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable("id") Long id) {
        var response = reservationService.findReservationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable("id") Long id, @RequestBody ReservationDTO reservationDTO) {
        var response = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDTO> deleteReservation(@PathVariable("id") Long id) {
        var response = reservationService.deleteReservation(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") Long id) {
        var response = reservationService.cancelReservation(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<ReservationDTO> fulfillReservation(@PathVariable("id") Long id) {
        var response = reservationService.fulfillReservation(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/expire")
    public ResponseEntity<ReservationDTO> expireReservation(@PathVariable("id") Long id) {
        var response = reservationService.expireReservation(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatus(@PathVariable("status") ReservationStatus status) {
        var response = reservationService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByMember(@PathVariable("memberId") Long memberId) {
        var response = reservationService.findByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByBook(@PathVariable("bookId") Long bookId) {
        var response = reservationService.findByBookId(bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}/active-count")
    public ResponseEntity<Long> getActiveReservationCountByMember(@PathVariable("memberId") Long memberId) {
        var response = reservationService.countActiveReservationsByMember(memberId);
        return ResponseEntity.ok(response);
    }
}
