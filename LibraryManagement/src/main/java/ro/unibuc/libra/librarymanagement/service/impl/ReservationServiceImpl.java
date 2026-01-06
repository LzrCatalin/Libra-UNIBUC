package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.entity.Reservation;
import ro.unibuc.libra.librarymanagement.mapper.ReservationMapper;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.repository.MemberRepository;
import ro.unibuc.libra.librarymanagement.repository.ReservationRepository;
import ro.unibuc.libra.librarymanagement.service.api.ReservationService;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  BookRepository bookRepository,
                                  MemberRepository memberRepository,
                                  ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Book book = bookRepository.findById(reservationDTO.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + reservationDTO.getBook().getId()));

        Member member = memberRepository.findById(reservationDTO.getMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + reservationDTO.getMember().getId()));

        if (book.getAvailableCopies() > 0) {
            throw new IllegalStateException("Book is available. No need to reserve.");
        }

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setMember(member);
        reservation.setReservationDate(ZonedDateTime.now());
        reservation.setExpiryDate(ZonedDateTime.now().plusDays(7));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setNotificationSend(false);

        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAllWithDetails()
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    @Override
    public ReservationDTO findReservationById(Long id) {
        return reservationMapper.toDTO(
                reservationRepository.findByIdWithDetails(id)
                        .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id))
        );
    }

    @Override
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));

        existingReservation.setReservationDate(reservationDTO.getReservationDate());
        existingReservation.setExpiryDate(reservationDTO.getExpiryDate());
        existingReservation.setStatus(reservationDTO.getStatus());
        existingReservation.setNotificationSend(reservationDTO.getNotificationSent());

        return reservationMapper.toDTO(reservationRepository.save(existingReservation));
    }

    @Override
    public ReservationDTO deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("Reservation not found with id: " + id);
        }

        ReservationDTO reservationDTO = reservationMapper.toDTO(
                reservationRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Reservation not found"))
        );

        reservationRepository.deleteById(id);

        return reservationDTO;
    }

    @Override
    public ReservationDTO cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only pending reservations can be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDTO fulfillReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only pending reservations can be fulfilled");
        }

        reservation.setStatus(ReservationStatus.FULFILLED);

        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDTO expireReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only pending reservations can be expired");
        }

        reservation.setStatus(ReservationStatus.EXPIRED);

        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationDTO> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status)
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findByBookId(Long bookId) {
        return reservationRepository.findByBookId(bookId)
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    @Override
    public Long countActiveReservationsByMember(Long memberId) {
        return reservationRepository.countByMemberIdAndStatus(memberId, ReservationStatus.PENDING);
    }
}
