package ro.unibuc.libra.librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.service.api.ReservationService;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.RESERVATIONS;

@Tag(name = "Reservations")
@RestController
@RequestMapping(RESERVATIONS)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        var response = reservationService.createReservation(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all reservations")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        var response = reservationService.getAllReservations();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reservation by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable("id") Long id) {
        var response = reservationService.findReservationById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable("id") Long id, @RequestBody ReservationDTO reservationDTO) {
        var response = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDTO> deleteReservation(@PathVariable("id") Long id) {
        var response = reservationService.deleteReservation(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") Long id) {
        var response = reservationService.cancelReservation(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Fulfill reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/fulfill")
    public ResponseEntity<ReservationDTO> fulfillReservation(@PathVariable("id") Long id) {
        var response = reservationService.fulfillReservation(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Expire reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "409")
    })
    @PostMapping("/{id}/expire")
    public ResponseEntity<ReservationDTO> expireReservation(@PathVariable("id") Long id) {
        var response = reservationService.expireReservation(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reservations by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatus(@PathVariable("status") ReservationStatus status) {
        var response = reservationService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reservations by member id")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByMember(@PathVariable("memberId") Long memberId) {
        var response = reservationService.findByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reservations by book id")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByBook(@PathVariable("bookId") Long bookId) {
        var response = reservationService.findByBookId(bookId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get active reservation count by member")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/member/{memberId}/active-count")
    public ResponseEntity<Long> getActiveReservationCountByMember(@PathVariable("memberId") Long memberId) {
        var response = reservationService.countActiveReservationsByMember(memberId);
        return ResponseEntity.ok(response);
    }
}
