package org.example.sistema_citas_medicas.logica.mappers.impl;

import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;
import org.example.sistema_citas_medicas.logica.dto.PacienteDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper implements Mapper<PacienteEntity, PacienteDto> {
    private final ModelMapper modelMapper;

    public PacienteMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PacienteDto mapTo(PacienteEntity pacienteEntity) {
        return modelMapper.map(pacienteEntity, PacienteDto.class);
    }

    @Override
    public PacienteEntity mapFrom(PacienteDto pacienteDto) {
        return modelMapper.map(pacienteDto, PacienteEntity.class);
    }
}