CREATE TABLE vaga_processada(
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    empresa VARCHAR(255) NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    senioridade VARCHAR(100)
);