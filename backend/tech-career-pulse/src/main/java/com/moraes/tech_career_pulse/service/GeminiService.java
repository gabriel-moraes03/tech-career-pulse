package com.moraes.tech_career_pulse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${ia.api.key}")
    private String apiKey;

    @Value("${ia.model}")
    private String model;

    @Value("${ia.max.tokens}")
    private Integer maxTokens;

    @Value("${ia.temperature}")
    private Double temperature;

    private final WebClient webClient;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta").build();
    }

    public Map<String, Object> chamarGemini(String prompt){
        try{
            String modelLimpo = this.model.trim();
            String keyLimpa = this.apiKey.trim();

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    ),
                    "generationConfig", Map.of(
                            "temperature", temperature,
                            "maxOutputTokens", maxTokens,
                            "responseMimeType", "application/json"
                    )
            );

            Map response = webClient.post()
                    // Usamos uriBuilder para evitar erros de concatenação de String
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .queryParam("key", keyLimpa)
                            .build(modelLimpo))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // Block é aceitável aqui se o Controller espera a resposta

            // 3. TRATAMENTO DE RESPOSTA MAIS SEGURO
            if (response == null) {
                throw new RuntimeException("Gemini retornou nulo.");
            }

            // Verifica se deu erro de prompt (ex: conteúdo bloqueado)
            if (response.containsKey("promptFeedback")) {
                Map feedback = (Map) response.get("promptFeedback");
                // Se tiver blockReason, lança erro
                if (feedback.containsKey("blockReason")) {
                    throw new RuntimeException("Conteúdo bloqueado pela IA: " + feedback.get("blockReason"));
                }
            }

            if (!response.containsKey("candidates")) {
                throw new RuntimeException("Resposta inválida da API (sem candidates): " + response);
            }

            List<Map> candidates = (List<Map>) response.get("candidates");
            if (candidates.isEmpty()) {
                throw new RuntimeException("A IA não gerou nenhuma sugestão.");
            }

            Map content = (Map) candidates.get(0).get("content");
            List <Map> parts = (List<Map>) content.get("parts");
            String texto = (String) parts.get(0).get("text");

            if (texto != null) {
                texto = texto.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();
            }

            Map usage = (Map) response.getOrDefault("usageMetadata", Collections.emptyMap());
            Integer tokens = (Integer) usage.getOrDefault("totalTokenCount", 0);

            System.out.println("JSON Limpo recebido da IA: " + texto);

            return Map.of("conteudo", texto, "tokens", tokens);
        } catch (Exception e) {
            throw new RuntimeException("Erro Gemini: " + e.getMessage(), e);
        }
    }
}
