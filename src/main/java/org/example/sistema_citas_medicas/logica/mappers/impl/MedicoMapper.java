package org.example.sistema_citas_medicas.logica.mappers.impl;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MedicoMapper implements Mapper<MedicoEntity, MedicoDto> {
    private ModelMapper modelMapper;

    public MedicoMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public MedicoDto mapTo(MedicoEntity medicoEntity) {
        MedicoDto dto = modelMapper.map(medicoEntity, MedicoDto.class);
        dto.setFotoPerfil(null); // ✅ No podemos mapear `byte[]` a `MultipartFile` directamente
        return dto;
    }

    @Override
    public MedicoEntity mapFrom(MedicoDto medicoDto) {
        MedicoEntity entity = modelMapper.map(medicoDto, MedicoEntity.class);
        try {
            if (medicoDto.getFotoPerfil() != null && !medicoDto.getFotoPerfil().isEmpty()) {
                entity.setFotoPerfil(medicoDto.getFotoPerfil().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al convertir la imagen", e);
        }
        return entity;
    }

}
