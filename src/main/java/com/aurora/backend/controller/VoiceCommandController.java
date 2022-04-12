package com.aurora.backend.controller;

import com.aurora.backend.dto.LiteralStringDto;
import com.aurora.backend.entity.VoiceCommand;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.VoiceCommandService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api
@RestController
@RequestMapping(value = "/voice/command")
public class VoiceCommandController extends BaseController<VoiceCommand, Long> {

    @Autowired
    private VoiceCommandService voiceCommandService;

    @Override
    protected BaseEntityService<VoiceCommand, Long> getService() {
        return voiceCommandService;
    }

    @Operation(summary = "Process voice command",
            description = "Process voice command",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping(value = {
            "/listen"
    }, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VoiceCommand> listen(final @RequestBody @Valid LiteralStringDto literalStringDto) {
        return ResponseEntity
                .ok()
                .body(voiceCommandService.processCommand(literalStringDto.request.trim()));
    }
}
