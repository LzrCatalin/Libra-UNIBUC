package ro.unibuc.libra.librarymanagement.mapper;

import ro.unibuc.libra.librarymanagement.config.GenericID;

import java.util.List;

public interface EntityMapper<DTO, ENTITY extends GenericID<ID>, ID> {

    ENTITY toEntity(DTO dto);

    DTO toDTO(ENTITY entity);

    List<DTO> toDTOs(List<ENTITY> entities);

    List<ENTITY> toEntities(List<DTO> dtos);

}
