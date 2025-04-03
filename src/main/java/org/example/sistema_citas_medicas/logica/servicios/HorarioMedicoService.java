package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HorarioMedicoService {

    List<HorarioMedicoDto> obtenerHorariosPorMedico(Long idMedico);

    HorarioMedicoDto obtenerHorarioPorId(Long idHorario);

    HorarioMedicoEntity guardarHorario(HorarioMedicoDto horarioDto);

    HorarioMedicoDto actualizarHorario(Long idHorario, HorarioMedicoDto horarioDto);

    void eliminarHorario(Long idHorario);

    Long obtenerIdMedicoPorHorario(Long idHorario);

}

