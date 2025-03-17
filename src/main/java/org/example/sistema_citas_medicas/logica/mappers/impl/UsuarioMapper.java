package org.example.sistema_citas_medicas.logica.mappers.impl;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper implements Mapper<UsuarioEntity, UsuarioDto> {
    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UsuarioDto mapTo(UsuarioEntity usuarioEntity) {
        return modelMapper.map(usuarioEntity, UsuarioDto.class);
    }

    @Override
    public UsuarioEntity mapFrom(UsuarioDto usuarioDto) {
        return modelMapper.map(usuarioDto, UsuarioEntity.class);
    }
}
