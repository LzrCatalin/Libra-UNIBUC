package ro.unibuc.libra.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.libra.librarymanagement.dto.FineDTO;
import ro.unibuc.libra.librarymanagement.entity.Fine;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FineMapper extends EntityMapper<FineDTO, Fine, Long> {
}
