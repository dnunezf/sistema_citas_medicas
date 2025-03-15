package org.example.sistema_citas_medicas.logic.mappers.impl;

import org.example.sistema_citas_medicas.data.entidades.Usuarios;
import org.example.sistema_citas_medicas.logic.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logic.mappers.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper implements Mappers<Usuarios, UsuarioDto> {
    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UsuarioDto mapTo(Usuarios usuario) {
        return modelMapper.map(usuario, UsuarioDto.class);
    }

    @Override
    public Usuarios mapFrom(UsuarioDto usuarioDto) {
        return modelMapper.map(usuarioDto, Usuarios.class);
    }
}
