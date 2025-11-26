package com.moraes.tech_career_pulse.dto;

import java.io.Serializable;

public record StatsGenericasDTO(
        String categoria,
        Long totalVagas
) implements Serializable {
    public StatsGenericasDTO(Object label, Long totalVagas) {
        this(label != null ? label.toString() : "Não especificado", totalVagas);
    }
}
