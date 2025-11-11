package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.entity.VagaProcessada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VagaProcessadaRepository extends JpaRepository<VagaProcessada, UUID> {
}
