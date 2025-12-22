package com.moraes.tech_career_pulse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.tech_career_pulse.entity.InteracaoIA;
import com.moraes.tech_career_pulse.entity.enums.FuncionalidadeIA;
import com.moraes.tech_career_pulse.repository.InteracaoIARepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InteracaoIAService {

    private final InteracaoIARepository interacaoIARepository;
    private final ObjectMapper objectMapper;

    public InteracaoIAService(InteracaoIARepository interacaoIARepository, ObjectMapper objectMapper) {
        this.interacaoIARepository = interacaoIARepository;
        this.objectMapper = objectMapper;
    }

    public <T> Optional<T> buscaCacheRecente(String hash, FuncionalidadeIA tipoInteracao, Class<T> classeRetorno){
        Optional<InteracaoIA> cacheDb = interacaoIARepository.findByTipoFuncionalidadeAndHashInput(tipoInteracao, hash);

        if(cacheDb.isPresent()){
            InteracaoIA interacaoIA = cacheDb.get();

            if(interacaoIA.getDataCriacao().isAfter(LocalDateTime.now().minusDays(7))){
                try{
                    System.out.println("Cache Hit no Banco: " + tipoInteracao);
                    return Optional.of(objectMapper.convertValue(interacaoIA.getRespostaIa(), classeRetorno));
                } catch (Exception e){
                    System.out.println("Erro ao converter JSON do cache: " + e.getMessage());
                }
            }
        }

        return Optional.empty();
    }

    public void salvarInteracao(String hash, Object requestDTO, Object responseDTO, FuncionalidadeIA tipo, String modelo, Integer tokens){
        try{
            InteracaoIA interacaoIA = new InteracaoIA();
            interacaoIA.setHashInput(hash);
            interacaoIA.setDataCriacao(LocalDateTime.now());
            interacaoIA.setParametrosInput(objectMapper.writeValueAsString(requestDTO));
            interacaoIA.setRespostaIa(objectMapper.writeValueAsString(responseDTO));
            interacaoIA.setTipoFuncionalidade(tipo);
            interacaoIA.setModeloIa(modelo);
            interacaoIA.setTokensUtilizados(tokens);
            interacaoIARepository.save(interacaoIA);
        } catch (Exception e) {
            System.err.println("Erro ao salvar log de IA (não crítico): " + e.getMessage());
        }
    }

    public String gerarHash(String rawContent){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(rawContent.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return  sb.toString();
        } catch (Exception e) {
            return String.valueOf(rawContent.hashCode());
        }
    }
}
