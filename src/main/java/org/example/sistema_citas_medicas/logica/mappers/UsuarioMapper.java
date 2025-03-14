package org.example.sistema_citas_medicas.logica.mappers;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.UsuarioDto;

public class UsuarioMapper {
    public static UsuarioDto toDTO(UsuarioEntity usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getRol().name()
        );
    }

    public static UsuarioEntity toEntity(UsuarioDto dto) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setRol(Enum.valueOf(UsuarioEntity.Rol.class, dto.getRol()));
        return usuario;
    }
}
