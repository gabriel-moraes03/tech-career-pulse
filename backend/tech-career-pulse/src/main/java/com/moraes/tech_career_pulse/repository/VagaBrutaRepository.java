package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.entity.VagaBruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VagaBrutaRepository extends JpaRepository<VagaBruta, UUID> {
}
