package ro.unibuc.libra.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.libra.librarymanagement.dto.LoanDTO;
import ro.unibuc.libra.librarymanagement.entity.Loan;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LoanMapper extends EntityMapper<LoanDTO, Loan, Long> {

    @Override
    @Mapping(target = "fine.loan", ignore = true)
    LoanDTO toDTO(Loan entity);
}
