package com.moraes.tech_career_pulse.dto;

import java.util.List;
import java.util.UUID;

public record VagaMatchDTO(
        UUID vagaId,
        String titulo,
        String empresa,
        String localizacao,
        String modelo,
        Integer compatibilidade,
        List<String> skillsMatch,
        List<String> skillsFaltam,
        String motivo,
        String url
) {}
