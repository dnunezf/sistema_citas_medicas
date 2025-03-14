package org.example.sistema_citas_medicas.logica.mappers.impl;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapperImpl implements Mapper<UsuarioEntity, UsuarioDto>
{
    private ModelMapper modelMapper;

    public UsuarioMapperImpl(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public UsuarioDto mapTo(UsuarioEntity usuarioEntity) {
        return modelMapper.map(usuarioEntity, UsuarioDto.class);
    }

    @Override
    public UsuarioEntity mapFrom(UsuarioDto authorDto) {
        return modelMapper.map(authorDto, UsuarioEntity.class);
    }
}
