ALTER TABLE vaga_processada
    RENAME COLUMN palavra_chave TO area;

ALTER TABLE vaga_processada
    ADD COLUMN url VARCHAR(1024) NOT NULL;