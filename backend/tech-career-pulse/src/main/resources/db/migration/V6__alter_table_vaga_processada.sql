ALTER TABLE vaga_processada
    ALTER COLUMN localizacao DROP NOT NULL,
    ADD COLUMN modelo VARCHAR(255) NOT NULL;