package com.moraes.tech_career_pulse.service;

import com.moraes.tech_career_pulse.dto.StatsGenericasDTO;
import com.moraes.tech_career_pulse.repository.VagaProcessadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private VagaProcessadaRepository repository;

    @Cacheable(value = "dashboard-skills")
    public List<StatsGenericasDTO> getVagasPorSkill(){
        return repository.countVagasPorSkill(PageRequest.of(0, 10));
    }

    @Cacheable(value = "dashboard-area")
    public List<StatsGenericasDTO> getVagasPorArea(){
        return repository.countVagasPorArea();
    }

    @Cacheable(value = "dashboard-modelo")
    public List<StatsGenericasDTO> getVagasPorModelo(){
        return repository.countVagaProcessadaPorModelo();
    }

    @Cacheable(value = "dashboard-senioridade")
    public List<StatsGenericasDTO> getVagasPorSenioridade(){
        return repository.countVagaProcessadaPorSenioridade();
    }

    @Cacheable(value = "dashboard-localizacao")
    public List<StatsGenericasDTO> getVagasPorLocalizacao(){
        return repository.countVagaProcessadaPorLocalizacao(PageRequest.of(0, 10));
    }
}
