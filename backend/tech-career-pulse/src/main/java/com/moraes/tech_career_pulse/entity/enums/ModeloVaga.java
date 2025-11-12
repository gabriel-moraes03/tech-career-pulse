package com.moraes.tech_career_pulse.entity.enums;

public enum Modelo {
    PRESENCIAL("Presencial"),
    HIBRIDO("Híbrido"),
    REMOTO("Remoto");

    private final String label;

    Modelo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
