package org.example.sistema_citas_medicas.logica.mappers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;

public class MedicoMapper {
    public static MedicoDto toDTO(MedicoEntity medico) {
        return new MedicoDto(
                medico.getId(),
                medico.getNombre(),
                medico.getRol().name(),
                medico.getEspecialidad(),
                medico.getCostoConsulta(),
                medico.getLocalidad(),
                medico.getFrecuenciaCitas(),
                medico.getPresentacion(),
                medico.getEstadoAprobacion().name()
        );
    }

    public static MedicoEntity toEntity(MedicoDto dto) {
        MedicoEntity medico = new MedicoEntity();
        medico.setId(dto.getId());
        medico.setNombre(dto.getNombre());
        medico.setRol(Enum.valueOf(UsuarioEntity.Rol.class, dto.getRol()));
        medico.setEspecialidad(dto.getEspecialidad());
        medico.setCostoConsulta(dto.getCostoConsulta());
        medico.setLocalidad(dto.getLocalidad());
        medico.setFrecuenciaCitas(dto.getFrecuenciaCitas());
        medico.setPresentacion(dto.getPresentacion());
        medico.setEstadoAprobacion(Enum.valueOf(MedicoEntity.EstadoAprobacion.class, dto.getEstadoAprobacion()));
        return medico;
    }
}
