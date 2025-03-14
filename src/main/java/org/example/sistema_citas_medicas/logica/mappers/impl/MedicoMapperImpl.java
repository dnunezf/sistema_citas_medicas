package org.example.sistema_citas_medicas.logica.mappers.impl;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapperImpl implements Mapper<MedicoEntity, MedicoDto>
{
    private ModelMapper modelMapper;

    public MedicoMapperImpl(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public MedicoDto mapTo(MedicoEntity medicoEntity) {
        return modelMapper.map(medicoEntity, MedicoDto.class);
    }

    @Override
    public MedicoEntity mapFrom(MedicoDto medicoDto) {
        return modelMapper.map(medicoDto, MedicoEntity.class);
    }

    public MedicoEntity mapFrom(MedicoDto medicoDto, UsuarioEntity usuario) {
        MedicoEntity medico = modelMapper.map(medicoDto, MedicoEntity.class);
        medico.setUsuarioEntity(usuario); // Asignar el usuario manualmente
        return medico;
    }
}
