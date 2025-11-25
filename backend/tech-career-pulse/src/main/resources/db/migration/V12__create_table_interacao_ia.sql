CREATE TABLE interacao_ia (
    id UUID PRIMARY KEY,

    -- O tipo de funcionalidade que o usuário usou
    -- Ex: 'ROADMAP', 'GERADOR_PROJETO', 'ANALISE_PERFIL'
    tipo_funcionalidade VARCHAR(50) NOT NULL,

    -- O que o usuário enviou. Como cada feature tem campos diferentes,
    -- usamos JSONB para ser flexível.
    -- Ex Roadmap: {"nivel": "Júnior", "stack": "Java", "foco": "Backend"}
    -- Ex Projeto: {"stack": ["React", "Python"], "tema": "E-commerce"}
    parametros_input JSONB NOT NULL,

    -- Para cache rápido (Opcional, mas Nível Sênior):
    -- Você gera um Hash (SHA256) dos parametros_input.
    -- Assim, para buscar se já existe, você busca pelo hash (texto curto)
    -- em vez de comparar JSONs gigantes.
    hash_input VARCHAR(64),

    -- A resposta da IA.
    -- Recomendo pedir pro Gemini devolver JSON e salvar como JSONB também.
    -- Assim o Front pode renderizar bonitinho (ex: Lista de passos do roadmap)
    -- em vez de um textão Markdown.
    resposta_ia JSONB NOT NULL,

    -- Metadados importantes
    modelo_ia VARCHAR(50) NOT NULL, -- Ex: 'gemini-1.5-flash'
    tokens_utilizados INTEGER, -- Para você controlar custo futuro
    data_criacao TIMESTAMP NOT NULL
);

-- Índice para deixar o cache instantâneo
CREATE INDEX idx_ia_cache ON interacao_ia(tipo_funcionalidade, hash_input);