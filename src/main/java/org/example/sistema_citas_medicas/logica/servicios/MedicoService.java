package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;

import java.util.Optional;

public interface MedicoService {

    Optional<MedicoEntity> obtenerPorId(Long id);
    MedicoEntity  actualizarMedico(MedicoEntity medico);
}