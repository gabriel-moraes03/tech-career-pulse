package com.moraes.tech_career_pulse.entity.enums;

public enum ModeloVaga {
    PRESENCIAL("Presencial"),
    HIBRIDO("Híbrido"),
    REMOTO("Remoto");

    private final String label;

    ModeloVaga(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
