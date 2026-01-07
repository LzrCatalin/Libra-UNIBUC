package ro.unibuc.libra.librarymanagement.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.mapper.FineMapper;
import ro.unibuc.libra.librarymanagement.mapper.LoanMapper;
import ro.unibuc.libra.librarymanagement.mapper.MemberMapper;
import ro.unibuc.libra.librarymanagement.mapper.ReservationMapper;
import ro.unibuc.libra.librarymanagement.repository.FineRepository;
import ro.unibuc.libra.librarymanagement.repository.LoanRepository;
import ro.unibuc.libra.librarymanagement.repository.MemberRepository;
import ro.unibuc.libra.librarymanagement.repository.ReservationRepository;
import ro.unibuc.libra.librarymanagement.service.api.MemberService;
import ro.unibuc.libra.librarymanagement.util.enums.MemberStatus;
import ro.unibuc.libra.librarymanagement.util.enums.MembershipType;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final LoanMapper loanMapper;
    private final LoanRepository loanRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final FineMapper fineMapper;
    private final FineRepository fineRepository;

    public MemberServiceImpl(MemberMapper memberMapper,
                             MemberRepository memberRepository,
                             LoanMapper loanMapper,
                             LoanRepository loanRepository,
                             ReservationMapper reservationMapper,
                             ReservationRepository reservationRepository,
                             FineMapper fineMapper,
                             FineRepository fineRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
        this.loanMapper = loanMapper;
        this.loanRepository = loanRepository;
        this.reservationMapper = reservationMapper;
        this.reservationRepository = reservationRepository;
        this.fineMapper = fineMapper;
        this.fineRepository = fineRepository;
    }

    @Override
    public MemberDTO findById(Long id) {
        return memberMapper.toDTO(
                memberRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Member not found with id: " + id
                        ))
        );
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper::toDTO)
                .toList();
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        return memberMapper.toDTO(
                memberRepository.save(memberMapper.toEntity(memberDTO))
        );
    }

    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found with id: " + id
                ));

        existingMember.setFirstName(memberDTO.getFirstName());
        existingMember.setLastName(memberDTO.getLastName());
        existingMember.setEmail(memberDTO.getEmail());
        existingMember.setPhoneNo(memberDTO.getPhoneNo());
        existingMember.setAddress(memberDTO.getAddress());
        existingMember.setMembershipDate(memberDTO.getMembershipDate());
        existingMember.setMembershipType(memberDTO.getMembershipType());
        existingMember.setStatus(memberDTO.getStatus());

        return memberMapper.toDTO(memberRepository.save(existingMember));
    }

    @Override
    public MemberDTO deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found with id: " + id
                ));

        MemberDTO memberDTO = memberMapper.toDTO(member);
        memberRepository.deleteById(id);
        return memberDTO;
    }

    @Override
    public List<MemberDTO> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(memberMapper::toDTO)
                .toList();
    }

    @Override
    public List<MemberDTO> searchByEmail(String email) {
        return memberRepository.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(memberMapper::toDTO)
                .toList();
    }

    @Override
    public List<MemberDTO> findByMembershipType(MembershipType membershipType) {
        return memberRepository.findByMembershipType(membershipType)
                .stream()
                .map(memberMapper::toDTO)
                .toList();
    }

    @Override
    public List<MemberDTO> findByStatus(MemberStatus status) {
        return memberRepository.findByStatus(status)
                .stream()
                .map(memberMapper::toDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> getMemberLoans(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Member not found with id: " + memberId
            );
        }

        return loanRepository.findByMemberId(memberId)
                .stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> getMemberReservations(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Member not found with id: " + memberId
            );
        }

        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    @Override
    public List<FineDTO> getMemberFines(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Member not found with id: " + memberId
            );
        }

        return fineRepository.findByMemberId(memberId)
                .stream()
                .map(fineMapper::toDTO)
                .toList();
    }
}
