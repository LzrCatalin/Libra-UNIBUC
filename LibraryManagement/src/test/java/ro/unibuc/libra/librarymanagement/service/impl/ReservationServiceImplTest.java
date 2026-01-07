package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.entity.Reservation;
import ro.unibuc.libra.librarymanagement.mapper.ReservationMapper;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.repository.MemberRepository;
import ro.unibuc.libra.librarymanagement.repository.ReservationRepository;
import ro.unibuc.libra.librarymanagement.util.enums.ReservationStatus;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;
    private ReservationDTO reservationDTO;
    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAvailableCopies(0);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");

        member = new Member();
        member.setId(1L);
        member.setFirstName("John");
        member.setLastName("Doe");

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setBook(book);
        reservation.setMember(member);
        reservation.setReservationDate(ZonedDateTime.now());
        reservation.setExpiryDate(ZonedDateTime.now().plusDays(7));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setNotificationSend(false);

        reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        reservationDTO.setBook(bookDTO);
        reservationDTO.setMember(memberDTO);
        reservationDTO.setReservationDate(ZonedDateTime.now());
        reservationDTO.setExpiryDate(ZonedDateTime.now().plusDays(7));
        reservationDTO.setStatus(ReservationStatus.PENDING);
        reservationDTO.setNotificationSent(false);
    }

    @Test
    void createReservation_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.createReservation(reservationDTO);

        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING, result.getStatus());
        verify(bookRepository).findById(1L);
        verify(memberRepository).findById(1L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void createReservation_BookNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(reservationDTO));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_MemberNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(reservationDTO));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_BookAvailable_ThrowsException() {
        book.setAvailableCopies(5);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThrows(IllegalStateException.class, () -> reservationService.createReservation(reservationDTO));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void getAllReservations_Success() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAllWithDetails()).thenReturn(reservations);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        List<ReservationDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findAllWithDetails();
    }

    @Test
    void getAllReservations_EmptyList() {
        when(reservationRepository.findAllWithDetails()).thenReturn(Collections.emptyList());

        List<ReservationDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findReservationById_Success() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.findReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reservationRepository).findByIdWithDetails(1L);
    }

    @Test
    void findReservationById_NotFound_ThrowsException() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.findReservationById(1L));
    }

    @Test
    void updateReservation_Success() {
        ReservationDTO updateDTO = new ReservationDTO();
        updateDTO.setReservationDate(ZonedDateTime.now());
        updateDTO.setExpiryDate(ZonedDateTime.now().plusDays(10));
        updateDTO.setStatus(ReservationStatus.FULFILLED);
        updateDTO.setNotificationSent(true);

        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(updateDTO);

        ReservationDTO result = reservationService.updateReservation(1L, updateDTO);

        assertNotNull(result);
        verify(reservationRepository).findByIdWithDetails(1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateReservation_NotFound_ThrowsException() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.updateReservation(1L, reservationDTO));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void deleteReservation_Success() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);
        doNothing().when(reservationRepository).deleteById(1L);

        ReservationDTO result = reservationService.deleteReservation(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void deleteReservation_NotFound_ThrowsException() {
        when(reservationRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> reservationService.deleteReservation(1L));
        verify(reservationRepository, never()).deleteById(anyLong());
    }

    @Test
    void cancelReservation_Success() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.cancelReservation(1L);

        assertNotNull(result);
        verify(reservationRepository).findByIdWithDetails(1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void cancelReservation_NotPending_ThrowsException() {
        reservation.setStatus(ReservationStatus.FULFILLED);
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.cancelReservation(1L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelReservation_NotFound_ThrowsException() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.cancelReservation(1L));
    }

    @Test
    void fulfillReservation_Success() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.fulfillReservation(1L);

        assertNotNull(result);
        verify(reservationRepository).findByIdWithDetails(1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void fulfillReservation_NotPending_ThrowsException() {
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.fulfillReservation(1L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void expireReservation_Success() {
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.expireReservation(1L);

        assertNotNull(result);
        verify(reservationRepository).findByIdWithDetails(1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void expireReservation_NotPending_ThrowsException() {
        reservation.setStatus(ReservationStatus.EXPIRED);
        when(reservationRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.expireReservation(1L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void findByStatus_Success() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findByStatus(ReservationStatus.PENDING)).thenReturn(reservations);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        List<ReservationDTO> result = reservationService.findByStatus(ReservationStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findByStatus(ReservationStatus.PENDING);
    }

    @Test
    void findByMemberId_Success() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findByMemberId(1L)).thenReturn(reservations);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        List<ReservationDTO> result = reservationService.findByMemberId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findByMemberId(1L);
    }

    @Test
    void findByBookId_Success() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findByBookId(1L)).thenReturn(reservations);
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        List<ReservationDTO> result = reservationService.findByBookId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findByBookId(1L);
    }

    @Test
    void countActiveReservationsByMember_Success() {
        when(reservationRepository.countByMemberIdAndStatus(1L, ReservationStatus.PENDING)).thenReturn(2L);

        Long result = reservationService.countActiveReservationsByMember(1L);

        assertEquals(2L, result);
        verify(reservationRepository).countByMemberIdAndStatus(1L, ReservationStatus.PENDING);
    }
}
