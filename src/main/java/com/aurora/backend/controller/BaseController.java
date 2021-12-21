package com.aurora.backend.controller;

import com.aurora.backend.entity.JpaEntity;
import com.aurora.backend.services.BaseEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public abstract class BaseController<E extends JpaEntity<ID>, ID extends Serializable> {

    protected abstract BaseEntityService<E, ID> getService();

    // --------------------------------------- get ---------------------------------------
    @Operation(summary = "Generic getById entity controller",
            description = "Generic getById entity controller",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping(value = {
            "/{id}"
    }, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<E> getById(final @PathVariable ID id) {
        return ResponseEntity
                .ok()
                .body(getService().getById(id));
    }

    // --------------------------------------- create ---------------------------------------

    @Operation(summary = "Generic create entity controller",
            description = "Generic create entity controller",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping(value = {
            ""
    }, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<E> create(final @RequestBody E e) {
        return ResponseEntity
                .ok()
                .body(getService().create(e));
    }

    // --------------------------------------- update ---------------------------------------

    @Operation(summary = "Generic update entity controller",
            description = "Generic update entity controller",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PutMapping(value = {
            "/{id}"
    }, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<E> update(final @RequestBody E e, final @PathVariable ID id) {
        return ResponseEntity
                .ok()
                .body(getService().update(id, e));
    }
}
