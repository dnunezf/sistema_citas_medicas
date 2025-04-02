package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HorarioMedicoService {

    // Obtener todos los horarios de un médico específico
    List<HorarioMedicoDto> obtenerHorariosPorMedico(Long idMedico);

    // Obtener un horario específico por su ID
    HorarioMedicoDto obtenerHorarioPorId(Long idHorario);

    // Guardar un nuevo horario para un médico
    HorarioMedicoEntity guardarHorario(HorarioMedicoDto horarioDto);

    // Actualizar un horario existente
    HorarioMedicoDto actualizarHorario(Long idHorario, HorarioMedicoDto horarioDto);

    // Eliminar un horario por su ID
    void eliminarHorario(Long idHorario);

    //List<LocalDateTime> generarEspacios(Long idMedico, List<HorarioMedicoDto> horarios);

}

