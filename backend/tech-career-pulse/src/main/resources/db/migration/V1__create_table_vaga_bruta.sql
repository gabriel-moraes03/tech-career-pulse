CREATE TABLE vaga_bruta(
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    empresa VARCHAR(255) NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    url VARCHAR(1024) NOT NULL UNIQUE,
    data_coleta TIMESTAMP NOT NULL,
    processada BOOLEAN DEFAULT FALSE
);