package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.dto.RegistroMedicoDto;

public interface MedicoService {
    MedicoDto registrarMedico(RegistroMedicoDto medicoDTO);
}