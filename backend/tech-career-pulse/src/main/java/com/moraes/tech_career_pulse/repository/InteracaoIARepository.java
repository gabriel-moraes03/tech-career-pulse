package com.moraes.tech_career_pulse.repository;

import com.moraes.tech_career_pulse.entity.InteracaoIA;
import com.moraes.tech_career_pulse.entity.enums.FuncionalidadeIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InteracaoIARepository extends JpaRepository<InteracaoIA, UUID> {
    @Query("SELECT i FROM InteracaoIA i WHERE i.tipoFuncionalidade = :tipo AND i.hashInput = :hash ORDER BY i.dataCriacao DESC LIMIT 1")
    Optional<InteracaoIA> findByTipoFuncionalidadeAndHashInput(
            @Param("tipo") FuncionalidadeIA tipo,
            @Param("hash") String hash
    );
}
