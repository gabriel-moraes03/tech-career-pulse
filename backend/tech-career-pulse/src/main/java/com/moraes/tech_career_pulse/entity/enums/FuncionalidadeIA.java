package com.moraes.tech_career_pulse.entity.enums;

public enum FuncionalidadeIA {
    ROADMAP("Roadmap para estudos"),
    PROJETO("Projeto para Portfólio"),
    MATCH("Match de Vagas");

    private final String label;

    FuncionalidadeIA(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
