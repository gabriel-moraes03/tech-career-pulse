package com.moraes.tech_career_pulse.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MatchRequestDTO(
    @NotEmpty(message="É necessário informar pelo menos uma skill")
    List<String> skills,

    @NotNull(message = "Senioridade é obrigatória")
    String senioridade,

    @NotNull(message = "Modelo de trabalho é obrigatório")
    String modelo,


    @NotNull(message = "Área de interesse é obrigatória")
    String area,

    String localizacao

) { }
