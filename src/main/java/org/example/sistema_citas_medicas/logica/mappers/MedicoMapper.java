package org.example.sistema_citas_medicas.logica.mappers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.dto.RegistroMedicoDto;

public class MedicoMapper {
    public static MedicoDto toDTO(MedicoEntity medico) {
        MedicoDto dto = new MedicoDto();
        dto.setId(medico.getId());
        dto.setNombre(medico.getNombre());
        dto.setEspecialidad(medico.getEspecialidad());
        dto.setCostoConsulta(medico.getCostoConsulta());
        dto.setLocalidad(medico.getLocalidad());
        dto.setFrecuenciaCitas(medico.getFrecuenciaCitas());
        dto.setPresentacion(medico.getPresentacion());
        dto.setEstadoAprobacion(medico.getEstadoAprobacion().name());
        return dto;
    }

    public static MedicoEntity toEntity(RegistroMedicoDto dto) {
        MedicoEntity medico = new MedicoEntity();
        medico.setNombre(dto.getNombre());
        medico.setClave(dto.getClave()); // La clave debe ser encriptada
        medico.setEspecialidad(dto.getEspecialidad());
        medico.setCostoConsulta(dto.getCostoConsulta());
        medico.setLocalidad(dto.getLocalidad());
        medico.setFrecuenciaCitas(dto.getFrecuenciaCitas());
        medico.setPresentacion(dto.getPresentacion());
        medico.setEstadoAprobacion(MedicoEntity.EstadoAprobacion.PENDIENTE);
        medico.setRol(UsuarioEntity.Rol.MEDICO);
        return medico;
    }
}
