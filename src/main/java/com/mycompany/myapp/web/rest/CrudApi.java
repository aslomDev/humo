package com.mycompany.myapp.web.rest;


import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.CrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/crud")
public class CrudApi {
    private final CrudService crudService;

    public CrudApi(CrudService crudService) {
        this.crudService = crudService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<ResponseEntity<?>> crud(){
       return Mono.just(ResponseEntity.ok(crudService.createCrud()));
    }
}
