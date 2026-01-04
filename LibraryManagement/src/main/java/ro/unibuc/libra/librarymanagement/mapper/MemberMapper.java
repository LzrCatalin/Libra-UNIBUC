package ro.unibuc.libra.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.libra.librarymanagement.dto.MemberDTO;
import ro.unibuc.libra.librarymanagement.entity.Member;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member, Long> {
}
