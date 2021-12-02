package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.HumoCard;
import com.mycompany.myapp.repository.HumoCardRepository;
import com.mycompany.myapp.service.HumoCardService;
import com.mycompany.myapp.service.dto.HumoCardDTO;
import com.mycompany.myapp.service.mapper.HumoCardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link HumoCard}.
 */
@Service
@Transactional
public class HumoCardServiceImpl implements HumoCardService {

    private final Logger log = LoggerFactory.getLogger(HumoCardServiceImpl.class);

    private final HumoCardRepository humoCardRepository;

    private final HumoCardMapper humoCardMapper;

    public HumoCardServiceImpl(HumoCardRepository humoCardRepository, HumoCardMapper humoCardMapper) {
        this.humoCardRepository = humoCardRepository;
        this.humoCardMapper = humoCardMapper;
    }

    @Override
    public Mono<HumoCardDTO> save(HumoCardDTO humoCardDTO) {
        log.debug("Request to save HumoCard : {}", humoCardDTO);
        return humoCardRepository.save(humoCardMapper.toEntity(humoCardDTO)).map(humoCardMapper::toDto);
    }

    @Override
    public Mono<HumoCardDTO> partialUpdate(HumoCardDTO humoCardDTO) {
        log.debug("Request to partially update HumoCard : {}", humoCardDTO);

        return humoCardRepository
            .findById(humoCardDTO.getId())
            .map(existingHumoCard -> {
                humoCardMapper.partialUpdate(existingHumoCard, humoCardDTO);

                return existingHumoCard;
            })
            .flatMap(humoCardRepository::save)
            .map(humoCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HumoCardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HumoCards");
        return humoCardRepository.findAllBy(pageable).map(humoCardMapper::toDto);
    }

    public Mono<Long> countAll() {
        return humoCardRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HumoCardDTO> findOne(Long id) {
        log.debug("Request to get HumoCard : {}", id);
        return humoCardRepository.findById(id).map(humoCardMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HumoCard : {}", id);
        return humoCardRepository.deleteById(id);
    }
}
