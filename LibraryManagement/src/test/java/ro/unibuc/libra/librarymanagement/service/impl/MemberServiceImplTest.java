package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.entity.Fine;
import ro.unibuc.libra.librarymanagement.entity.Loan;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.entity.Reservation;
import ro.unibuc.libra.librarymanagement.mapper.FineMapper;
import ro.unibuc.libra.librarymanagement.mapper.LoanMapper;
import ro.unibuc.libra.librarymanagement.mapper.MemberMapper;
import ro.unibuc.libra.librarymanagement.mapper.ReservationMapper;
import ro.unibuc.libra.librarymanagement.repository.FineRepository;
import ro.unibuc.libra.librarymanagement.repository.LoanRepository;
import ro.unibuc.libra.librarymanagement.repository.MemberRepository;
import ro.unibuc.libra.librarymanagement.repository.ReservationRepository;
import ro.unibuc.libra.librarymanagement.util.enums.MemberStatus;
import ro.unibuc.libra.librarymanagement.util.enums.MembershipType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private FineMapper fineMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNo("1234567890");
        member.setAddress("123 Test St");
        member.setMembershipDate(LocalDate.now());
        member.setMembershipType(MembershipType.STANDARD);
        member.setStatus(MemberStatus.ACTIVE);

        memberDTO = new MemberDTO();
        memberDTO.setId(1L);
        memberDTO.setFirstName("John");
        memberDTO.setLastName("Doe");
        memberDTO.setEmail("john.doe@example.com");
        memberDTO.setPhoneNo("1234567890");
        memberDTO.setAddress("123 Test St");
        memberDTO.setMembershipDate(LocalDate.now());
        memberDTO.setMembershipType(MembershipType.STANDARD);
        memberDTO.setStatus(MemberStatus.ACTIVE);
    }

    @Test
    void findById_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = memberService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(memberRepository).findById(1L);
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> memberService.findById(1L));
    }

    @Test
    void getAllMembers_Success() {
        List<Member> members = List.of(member);
        when(memberRepository.findAll()).thenReturn(members);
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        List<MemberDTO> result = memberService.getAllMembers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).findAll();
    }

    @Test
    void getAllMembers_EmptyList() {
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());

        List<MemberDTO> result = memberService.getAllMembers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createMember_Success() {
        when(memberMapper.toEntity(memberDTO)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = memberService.createMember(memberDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(memberRepository).save(member);
    }

    @Test
    void updateMember_Success() {
        MemberDTO updateDTO = new MemberDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setLastName("Smith");
        updateDTO.setEmail("jane.smith@example.com");
        updateDTO.setPhoneNo("0987654321");
        updateDTO.setAddress("456 New St");
        updateDTO.setMembershipDate(LocalDate.now());
        updateDTO.setMembershipType(MembershipType.PREMIUM);
        updateDTO.setStatus(MemberStatus.ACTIVE);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toDTO(member)).thenReturn(updateDTO);

        MemberDTO result = memberService.updateMember(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(memberRepository).findById(1L);
        verify(memberRepository).save(member);
    }

    @Test
    void updateMember_NotFound_ThrowsException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> memberService.updateMember(1L, memberDTO));
        verify(memberRepository, never()).save(any());
    }

    @Test
    void deleteMember_Success() {
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);
        doNothing().when(memberRepository).deleteById(1L);

        MemberDTO result = memberService.deleteMember(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(memberRepository).deleteById(1L);
    }

    @Test
    void deleteMember_NotFound_ThrowsException() {
        when(memberRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> memberService.deleteMember(1L));
        verify(memberRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchByName_Success() {
        List<Member> members = List.of(member);
        when(memberRepository.findByNameContainingIgnoreCase("John")).thenReturn(members);
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        List<MemberDTO> result = memberService.searchByName("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    void searchByEmail_Success() {
        List<Member> members = List.of(member);
        when(memberRepository.findByEmailContainingIgnoreCase("john")).thenReturn(members);
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        List<MemberDTO> result = memberService.searchByEmail("john");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).findByEmailContainingIgnoreCase("john");
    }

    @Test
    void findByMembershipType_Success() {
        List<Member> members = List.of(member);
        when(memberRepository.findByMembershipType(MembershipType.STANDARD)).thenReturn(members);
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        List<MemberDTO> result = memberService.findByMembershipType(MembershipType.STANDARD);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).findByMembershipType(MembershipType.STANDARD);
    }

    @Test
    void findByStatus_Success() {
        List<Member> members = List.of(member);
        when(memberRepository.findByStatus(MemberStatus.ACTIVE)).thenReturn(members);
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        List<MemberDTO> result = memberService.findByStatus(MemberStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).findByStatus(MemberStatus.ACTIVE);
    }

    @Test
    void getMemberLoans_Success() {
        Loan loan = new Loan();
        loan.setId(1L);
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setId(1L);

        when(memberRepository.existsById(1L)).thenReturn(true);
        when(loanRepository.findByMemberId(1L)).thenReturn(List.of(loan));
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);

        List<LoanDTO> result = memberService.getMemberLoans(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).existsById(1L);
        verify(loanRepository).findByMemberId(1L);
    }

    @Test
    void getMemberLoans_MemberNotFound_ThrowsException() {
        when(memberRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> memberService.getMemberLoans(1L));
        verify(loanRepository, never()).findByMemberId(anyLong());
    }

    @Test
    void getMemberReservations_Success() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);

        when(memberRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findByMemberId(1L)).thenReturn(List.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        List<ReservationDTO> result = memberService.getMemberReservations(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).existsById(1L);
        verify(reservationRepository).findByMemberId(1L);
    }

    @Test
    void getMemberReservations_MemberNotFound_ThrowsException() {
        when(memberRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> memberService.getMemberReservations(1L));
        verify(reservationRepository, never()).findByMemberId(anyLong());
    }

    @Test
    void getMemberFines_Success() {
        Fine fine = new Fine();
        fine.setId(1L);
        FineDTO fineDTO = new FineDTO();
        fineDTO.setId(1L);

        when(memberRepository.existsById(1L)).thenReturn(true);
        when(fineRepository.findByMemberId(1L)).thenReturn(List.of(fine));
        when(fineMapper.toDTO(fine)).thenReturn(fineDTO);

        List<FineDTO> result = memberService.getMemberFines(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository).existsById(1L);
        verify(fineRepository).findByMemberId(1L);
    }

    @Test
    void getMemberFines_MemberNotFound_ThrowsException() {
        when(memberRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> memberService.getMemberFines(1L));
        verify(fineRepository, never()).findByMemberId(anyLong());
    }
}
