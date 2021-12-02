package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.HumoClient;
import com.mycompany.myapp.service.dto.HumoClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HumoClient} and its DTO {@link HumoClientDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HumoClientMapper extends EntityMapper<HumoClientDTO, HumoClient> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HumoClientDTO toDtoId(HumoClient humoClient);
}
