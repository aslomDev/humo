package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.HumoCard;
import com.mycompany.myapp.service.dto.HumoCardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HumoCard} and its DTO {@link HumoCardDTO}.
 */
@Mapper(componentModel = "spring", uses = { HumoClientMapper.class })
public interface HumoCardMapper extends EntityMapper<HumoCardDTO, HumoCard> {
    @Mapping(target = "humoClient", source = "humoClient", qualifiedByName = "id")
    HumoCardDTO toDto(HumoCard s);
}
