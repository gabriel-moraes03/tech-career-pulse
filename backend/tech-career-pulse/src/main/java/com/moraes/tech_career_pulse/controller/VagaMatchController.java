package com.moraes.tech_career_pulse.controller;

import com.moraes.tech_career_pulse.dto.MatchRequestDTO;
import com.moraes.tech_career_pulse.dto.MatchResponseDTO;
import com.moraes.tech_career_pulse.service.VagaMatchService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/vagas")
@Tag(name = "Match de Vagas", description = "Endpoints para recomendação inteligente de vagas")
public class VagaMatchController {

    private final VagaMatchService vagaMatchService;

    public VagaMatchController(VagaMatchService vagaMatchService) {
        this.vagaMatchService = vagaMatchService;
    }

    @PostMapping("/match")
    @Operation(summary = "Realiza o match inteligente", description = "Recebe skills e perfil, busca vagas no banco e usa IA para ranquear e justificar.")
    public ResponseEntity<MatchResponseDTO> buscarMatch(@RequestBody @Valid MatchRequestDTO request) {
        if(request.skills() == null || request.skills().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        MatchResponseDTO response = vagaMatchService.encontrarVagas(request);

        return ResponseEntity.ok(response);
    }
}
