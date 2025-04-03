package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;

import java.util.List;
import java.util.Optional;

public interface PacienteService {
    PacienteEntity obtenerPorId(Long id);

    void actualizarPaciente(PacienteEntity paciente);

    Optional<PacienteEntity> findOne(Long id);
}

