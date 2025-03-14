package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;

public interface MedicoService {
    MedicoEntity registrarMedico(MedicoDto medicoDTO);
}