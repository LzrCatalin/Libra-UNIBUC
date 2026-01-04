package ro.unibuc.libra.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.libra.librarymanagement.dto.ReservationDTO;
import ro.unibuc.libra.librarymanagement.entity.Reservation;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation, Long> {
}
