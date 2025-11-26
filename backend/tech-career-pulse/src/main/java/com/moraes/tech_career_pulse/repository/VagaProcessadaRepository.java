package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.dto.StatsGenericasDTO;
import com.moraes.tech_career_pulse.entity.VagaProcessada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface VagaProcessadaRepository extends JpaRepository<VagaProcessada, UUID> {
    @Query("SELECT new com.moraes.tech_career_pulse.dto.StatsGenericasDTO(s.nome, COUNT(v)) " +
            "FROM VagaProcessada v JOIN v.skills s " +
            "GROUP BY s.nome ORDER BY COUNT(v) DESC")
    List<StatsGenericasDTO> countVagasPorSkill(Pageable pageable);

    @Query("SELECT new com.moraes.tech_career_pulse.dto.StatsGenericasDTO(v.area, COUNT(v)) " +
            "FROM VagaProcessada v GROUP BY v.area ORDER BY COUNT(v) DESC")
    List<StatsGenericasDTO> countVagasPorArea();

    @Query("SELECT new com.moraes.tech_career_pulse.dto.StatsGenericasDTO(v.modelo, COUNT(v))" +
            "FROM VagaProcessada v GROUP BY v.modelo ORDER BY COUNT(v) DESC")
    List<StatsGenericasDTO> countVagaProcessadaPorModelo();

    @Query("SELECT new com.moraes.tech_career_pulse.dto.StatsGenericasDTO(v.localizacao, COUNT(v))" +
            "FROM VagaProcessada v GROUP BY v.localizacao ORDER BY COUNT(v) DESC")
    List<StatsGenericasDTO> countVagaProcessadaPorLocalizacao(Pageable pageable);

    @Query("SELECT new com.moraes.tech_career_pulse.dto.StatsGenericasDTO(v.senioridade, COUNT(v))" +
            "FROM VagaProcessada v GROUP BY v.senioridade ORDER BY COUNT(v) DESC")
    List<StatsGenericasDTO> countVagaProcessadaPorSenioridade();
}
