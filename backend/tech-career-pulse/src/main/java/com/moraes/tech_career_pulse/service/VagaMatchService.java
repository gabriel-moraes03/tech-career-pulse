package com.moraes.tech_career_pulse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.tech_career_pulse.dto.MatchRequestDTO;
import com.moraes.tech_career_pulse.dto.MatchResponseDTO;
import com.moraes.tech_career_pulse.dto.VagaMatchDTO;
import com.moraes.tech_career_pulse.entity.VagaProcessada;
import com.moraes.tech_career_pulse.entity.enums.FuncionalidadeIA;
import com.moraes.tech_career_pulse.entity.enums.ModeloVaga;
import com.moraes.tech_career_pulse.repository.VagaProcessadaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VagaMatchService {

    private final VagaProcessadaRepository vagaProcessadaRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    private final InteracaoIAService interacaoIAService;

    private static final String MODELO_ATUAL = "gemini-1.5-flash";

    public VagaMatchService(VagaProcessadaRepository vagaRepository, GeminiService  geminiService, ObjectMapper objectMapper, InteracaoIAService interacaoIAService) {
        this.vagaProcessadaRepository = vagaRepository;
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
        this.interacaoIAService = interacaoIAService;
    }

    public MatchResponseDTO encontrarVagas(MatchRequestDTO request){
        String hashInput = gerarHashDoPerfil(request);

        Optional<MatchResponseDTO> cache = interacaoIAService.buscaCacheRecente(hashInput, FuncionalidadeIA.MATCH, MatchResponseDTO.class);

        if (cache.isPresent()){
            return cache.get();
        }

        ModeloVaga modeloEnum = request.modelo() != null ? ModeloVaga.fromLabel(request.modelo()) : null;
        List<VagaProcessada> potenciaisVagas = vagaProcessadaRepository.findVagasCandidatas(request.skills(), request.senioridade(), modeloEnum, PageRequest.of(0, 30));

        if (potenciaisVagas.isEmpty()) return new MatchResponseDTO(List.of(), 0, false, 0, "Sem consulta com Ia");

        String jsonVagas = potenciaisVagas.stream().map(v -> String.format(
                "{\"id\":\"%s\", \"titulo\":\"%s\", \"empresa\":\"%s\", \"skills\":%s}",
                v.getId(), v.getTitulo(), v.getEmpresa(),
                v.getSkills().stream().map(s -> "\"" + s.getNome() + "\"").toList()
        )).collect(Collectors.joining(", ", "[", "]"));

        String prompt = """
            Você é um Recrutador Tech Sênior.
            CANDIDATO: Skills: %s | Senioridade: %s | Modelo: %s
            VAGAS DISPONÍVEIS (JSON): %s
            
            TAREFA:
            Analise as vagas e retorne um JSON array com os TOP 5 melhores matches.
            Regras:
            1. O campo "motivo" deve ter no máximo 20 palavras.
            2. Retorne APENAS o JSON cru, sem markdown (```json).
            
            Estrutura de saída:
            [{"vagaId": "UUID", "compatibilidade": 0-100, "skillsMatch": [], "skillsFaltam": [], "motivo": "Curto e direto"}]
            """.formatted(request.skills(), request.senioridade(), request.modelo(), jsonVagas);

        Map<String, Object> respostaGemini = geminiService.chamarGemini(prompt);
        String conteudoTexto = (String) respostaGemini.get("conteudo");
        Integer tokens = (Integer) respostaGemini.get("tokens");

        try{
            String jsonLimpo = conteudoTexto.replace("```json", "").replace("```", "").trim();
            List<IaMatchResult> resultadosIa = objectMapper.readValue(jsonLimpo, new TypeReference<>() {});
            List<VagaMatchDTO> listaMatches = new ArrayList<>();

            for(IaMatchResult result : resultadosIa){
                VagaProcessada original = potenciaisVagas.stream().filter(v -> v.getId().equals(UUID.fromString(result.vagaId))).findFirst().orElse(null);

                if (original != null){
                    String modeloTexto = original.getModelo() != null
                            ? original.getModelo().getLabel()
                            : "Não especificado";

                    listaMatches.add(new VagaMatchDTO(
                            original.getId(),
                            original.getTitulo(),
                            original.getEmpresa(),
                            original.getLocalizacao(), // <--- NOVO: Pegamos do banco
                            modeloTexto,               // <--- NOVO: Pegamos do Enum do banco
                            result.compatibilidade,       // Vem da IA
                            result.skillsMatch,           // Vem da IA
                            result.skillsFaltam,          // Vem da IA
                            result.motivo,                // Vem da IA
                            original.getUrl()
                    ));
                }
            }

            listaMatches.sort((a,b) -> b.compatibilidade().compareTo(a.compatibilidade()));

            MatchResponseDTO responseFinal = new MatchResponseDTO(
                    listaMatches,
                    listaMatches.size(),
                    false,
                    tokens,
                    MODELO_ATUAL
            );

            interacaoIAService.salvarInteracao(
                    hashInput,
                    request,
                    responseFinal,
                    FuncionalidadeIA.MATCH,
                    "gemini-2.5-flash",
                    tokens
            );

            return responseFinal;
        } catch (Exception e){
            throw new RuntimeException("Erro processamento IA: " + e.getMessage());
        }
    }

    private String gerarHashDoPerfil(MatchRequestDTO request) {
        List<String> skillsSorted = new ArrayList<>(request.skills());
        Collections.sort(skillsSorted);
        String rawKey = String.join("|",
                skillsSorted.toString(), request.senioridade(), request.modelo(), request.area()
        );
        // Usa o utilitário do service para fazer o MD5
        return interacaoIAService.gerarHash(rawKey);
    }

    private static class IaMatchResult {
        public String vagaId;
        public Integer compatibilidade;
        public List<String> skillsMatch;
        public List<String> skillsFaltam;
        public String motivo;
    }
}
