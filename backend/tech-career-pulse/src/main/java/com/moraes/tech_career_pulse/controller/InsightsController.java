package com.moraes.tech_career_pulse.controller;

import com.moraes.tech_career_pulse.dto.StatsGenericasDTO;
import com.moraes.tech_career_pulse.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
public class InsightsController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/skills")
    public ResponseEntity<List<StatsGenericasDTO>> getVagasPorSkill(){
        return ResponseEntity.ok(dashboardService.getVagasPorSkill());
    }

    @GetMapping("/area")
    public ResponseEntity<List<StatsGenericasDTO>> getVagasPorArea(){
        return ResponseEntity.ok(dashboardService.getVagasPorArea());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<StatsGenericasDTO>> getVagasPorModelo(){
        return ResponseEntity.ok(dashboardService.getVagasPorModelo());
    }

    @GetMapping("/localizacao")
    public ResponseEntity<List<StatsGenericasDTO>> getVagasPorLocalizacao(){
        return ResponseEntity.ok(dashboardService.getVagasPorLocalizacao());
    }

    @GetMapping("/senioridade")
    public ResponseEntity<List<StatsGenericasDTO>> getVagasPorSenioridade(){
        return ResponseEntity.ok(dashboardService.getVagasPorSenioridade());
    }
}
