package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.logica.dto.MedicoDto;

public interface MedicoService {
    MedicoDto registrarMedico(MedicoDto medicoDTO);
}