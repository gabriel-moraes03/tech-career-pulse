package com.moraes.tech_career_pulse.entity;

import com.moraes.tech_career_pulse.entity.enums.ModeloVaga;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vaga_processada")
@Getter
@Setter
@NoArgsConstructor
public class VagaProcessada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String empresa;

    private String senioridade;

    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeloVaga modelo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vaga_skill_rel",
            joinColumns = @JoinColumn(name = "vaga_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    public VagaProcessada(String titulo, String empresa, String senioridade, String localizacao, ModeloVaga modelo) {
        this.titulo = titulo;
        this.empresa = empresa;
        this.senioridade = senioridade;
        this.localizacao = localizacao;
        this.modelo = modelo;
    }
}
