CREATE TABLE vaga_skill_rel(
    vaga_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    PRIMARY KEY (vaga_id, skill_id),
    FOREIGN KEY (vaga_id) REFERENCES vaga_processada(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);