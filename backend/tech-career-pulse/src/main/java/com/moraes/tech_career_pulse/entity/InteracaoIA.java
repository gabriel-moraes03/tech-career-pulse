package com.moraes.tech_career_pulse.entity;

import com.moraes.tech_career_pulse.entity.enums.FuncionalidadeIA;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="interacao_ia")
@Getter
@Setter
@NoArgsConstructor
public class InteracaoIA {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(name = "parametros_input", nullable = false, columnDefinition = "jsonb")
    private String parametrosInput;

    @Column(name="hash_input", length = 64)
    private String hashInput;

    @Column(name = "resposta_ia", nullable = false, columnDefinition = "jsonb")
    private String respostaIa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_funcionalidade",nullable = false)
    private FuncionalidadeIA tipoFuncionalidade;

    @Column(nullable = false, name = "modelo_ia")
    private String modeloIa;

    @Column(name = "tokens_utilizados", nullable = false)
    private Integer tokensUtilizados;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public InteracaoIA(String parametrosInput, String hashInput, String respostaIa, FuncionalidadeIA tipoFuncionalidade, String modeloIa) {
        this.parametrosInput = parametrosInput;
        this.hashInput = hashInput;
        this.respostaIa = respostaIa;
        this.tipoFuncionalidade = tipoFuncionalidade;
        this.modeloIa = modeloIa;
        this.tokensUtilizados = tokensUtilizados != null ? tokensUtilizados : 0;
        this.dataCriacao = LocalDateTime.now();
    }
}
