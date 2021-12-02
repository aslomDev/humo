package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.HumoClient;
import com.mycompany.myapp.repository.HumoClientRepository;
import com.mycompany.myapp.service.HumoClientService;
import com.mycompany.myapp.service.dto.HumoClientDTO;
import com.mycompany.myapp.service.mapper.HumoClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link HumoClient}.
 */
@Service
@Transactional
public class HumoClientServiceImpl implements HumoClientService {

    private final Logger log = LoggerFactory.getLogger(HumoClientServiceImpl.class);

    private final HumoClientRepository humoClientRepository;

    private final HumoClientMapper humoClientMapper;

    public HumoClientServiceImpl(HumoClientRepository humoClientRepository, HumoClientMapper humoClientMapper) {
        this.humoClientRepository = humoClientRepository;
        this.humoClientMapper = humoClientMapper;
    }

    @Override
    public Mono<HumoClientDTO> save(HumoClientDTO humoClientDTO) {
        log.debug("Request to save HumoClient : {}", humoClientDTO);
        return humoClientRepository.save(humoClientMapper.toEntity(humoClientDTO)).map(humoClientMapper::toDto);
    }

    @Override
    public Mono<HumoClientDTO> partialUpdate(HumoClientDTO humoClientDTO) {
        log.debug("Request to partially update HumoClient : {}", humoClientDTO);

        return humoClientRepository
            .findById(humoClientDTO.getId())
            .map(existingHumoClient -> {
                humoClientMapper.partialUpdate(existingHumoClient, humoClientDTO);

                return existingHumoClient;
            })
            .flatMap(humoClientRepository::save)
            .map(humoClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HumoClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HumoClients");
        return humoClientRepository.findAllBy(pageable).map(humoClientMapper::toDto);
    }

    public Mono<Long> countAll() {
        return humoClientRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HumoClientDTO> findOne(Long id) {
        log.debug("Request to get HumoClient : {}", id);
        return humoClientRepository.findById(id).map(humoClientMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HumoClient : {}", id);
        return humoClientRepository.deleteById(id);
    }
}
