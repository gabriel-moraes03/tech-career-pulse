package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.entity.InteracaoIA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InteracaoIARepository extends JpaRepository<InteracaoIA, UUID> {
}
