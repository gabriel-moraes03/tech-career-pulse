package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.dto.StatsGenericasDTO;
import com.moraes.tech_career_pulse.entity.VagaProcessada;
import com.moraes.tech_career_pulse.entity.enums.ModeloVaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT DISTINCT v
        FROM VagaProcessada v
        JOIN v.skills s
        WHERE s.nome IN :skills
        AND (v.senioridade = :senioridade OR v.senioridade IS NULL)
        AND (v.modelo = :modelo or v.modelo IS NULL)
    """)
    List<VagaProcessada> findVagasCandidatas(
            @Param("skills") List<String> skills,
            @Param("senioridade") String senioridade,
            @Param("modelo") ModeloVaga modelo,
            Pageable pageable
    );
}
