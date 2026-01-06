package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.util.List;

public interface ReservationService {

    ReservationDTO createReservation(ReservationDTO reservationDTO);
    List<ReservationDTO> getAllReservations();
    ReservationDTO findReservationById(Long id);
    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO);
    ReservationDTO deleteReservation(Long id);

    ReservationDTO cancelReservation(Long id);
    ReservationDTO fulfillReservation(Long id);
    ReservationDTO expireReservation(Long id);

    List<ReservationDTO> findByStatus(ReservationStatus status);
    List<ReservationDTO> findByMemberId(Long memberId);
    List<ReservationDTO> findByBookId(Long bookId);

    Long countActiveReservationsByMember(Long memberId);
}
