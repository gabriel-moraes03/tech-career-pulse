package com.moraes.tech_career_pulse.entity.enums;

import com.moraes.tech_career_pulse.entity.enums.ModeloVaga;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // <--- O PULO DO GATO: Aplica sozinho em todo lugar!
public class ModeloVagaConverter implements AttributeConverter<ModeloVaga, String> {

    @Override
    public String convertToDatabaseColumn(ModeloVaga attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getLabel(); // Vai salvar "Híbrido" no banco
    }

    @Override
    public ModeloVaga convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return ModeloVaga.fromLabel(dbData); // Vai ler "Híbrido" e virar Enum.HIBRIDO
    }
}
