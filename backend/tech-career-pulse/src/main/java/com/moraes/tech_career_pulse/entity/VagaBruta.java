package com.moraes.tech_career_pulse.entity;

import com.moraes.tech_career_pulse.entity.enums.ModeloVaga;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="vaga_bruta")
@Getter
@Setter
@NoArgsConstructor
public class VagaBruta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String empresa;

    private String localizacao;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(name = "data_coleta", nullable = false)
    private LocalDateTime dataColeta;

    @Column(nullable = false)
    private boolean processada = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeloVaga modelo;

    public VagaBruta(String titulo, String descricao, String empresa, String localizacao, String url, LocalDateTime dataColeta, ModeloVaga modelo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.empresa = empresa;
        this.localizacao = localizacao;
        this.url = url;
        this.dataColeta = dataColeta;
        this.processada = false;
        this.modelo = modelo;
    }
}
