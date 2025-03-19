package org.example.sistema_citas_medicas.logica.mappers.impl;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class HorarioMedicoMapper implements Mapper<HorarioMedicoEntity, HorarioMedicoDto> {
    private final ModelMapper modelMapper;

    public HorarioMedicoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public HorarioMedicoDto mapTo(HorarioMedicoEntity horarioMedicoEntity) {
        return new HorarioMedicoDto(
                horarioMedicoEntity.getId(),
                horarioMedicoEntity.getMedico().getId(),
                horarioMedicoEntity.getDiaSemana().name(), // Convertir ENUM a String
                horarioMedicoEntity.getHoraInicio(),
                horarioMedicoEntity.getHoraFin(),
                horarioMedicoEntity.getTiempoCita()
        );
    }

    @Override
    public HorarioMedicoEntity mapFrom(HorarioMedicoDto horarioMedicoDto) {
        HorarioMedicoEntity entity = new HorarioMedicoEntity();
        entity.setId(horarioMedicoDto.getId());
        entity.setHoraInicio(horarioMedicoDto.getHoraInicio());
        entity.setHoraFin(horarioMedicoDto.getHoraFin());
        entity.setTiempoCita(horarioMedicoDto.getTiempoCita());

        try {
            entity.setDiaSemana(HorarioMedicoEntity.DiaSemana.valueOf(horarioMedicoDto.getDiaSemana()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error: Día de la semana inválido - " + horarioMedicoDto.getDiaSemana());
        }

        return entity;
    }
}
