package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
}
