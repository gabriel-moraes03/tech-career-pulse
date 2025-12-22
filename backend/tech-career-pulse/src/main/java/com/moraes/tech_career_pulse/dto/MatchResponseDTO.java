package com.moraes.tech_career_pulse.dto;

import java.util.List;

public record MatchResponseDTO(
        List<VagaMatchDTO> matches,
        Integer totalMatches,
        Boolean fromCache,
        Integer tokensUtilizados,
        String modeloIa
) {
}
