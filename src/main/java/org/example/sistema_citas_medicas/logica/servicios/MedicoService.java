package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;

import java.util.List;
import java.util.Optional;

public interface MedicoService {

    Optional<MedicoEntity> obtenerPorId(Long id);
    MedicoEntity  actualizarMedico(MedicoEntity medico);
    List<MedicoEntity> obtenerTodosMedicos();
    void actualizarEstadoAprobacion(Long id, MedicoEntity.EstadoAprobacion estado);

    List<MedicoDto> buscarPorEspecialidadYUbicacion(String especialidad, String ubicacion);

    // Obtener todas las especialidades disponibles
    List<String> obtenerEspecialidades();


}