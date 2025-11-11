package com.moraes.tech_career_pulse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="skill")
@Getter
@Setter
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToMany(mappedBy = "skills")
    private Set<VagaProcessada> vagas = new HashSet<>();

    public Skill(String nome) {
        this.nome = nome;
    }
}
