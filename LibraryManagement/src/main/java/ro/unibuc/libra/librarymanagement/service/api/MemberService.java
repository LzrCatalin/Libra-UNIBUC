package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.util.enums.MemberStatus;
import ro.unibuc.libra.librarymanagement.util.enums.MembershipType;

import java.util.List;

public interface MemberService {

    MemberDTO findById(Long id);
    List<MemberDTO> getAllMembers();
    MemberDTO createMember(MemberDTO memberDTO);
    MemberDTO updateMember(Long id, MemberDTO memberDTO);
    MemberDTO deleteMember(Long id);
    List<MemberDTO> searchByName(String name);
    List<MemberDTO> searchByEmail(String email);
    List<MemberDTO> findByMembershipType(MembershipType membershipType);
    List<MemberDTO> findByStatus(MemberStatus status);
    List<LoanDTO> getMemberLoans(Long memberId);
    List<ReservationDTO> getMemberReservations(Long memberId);
    List<FineDTO> getMemberFines(Long memberId);
}
