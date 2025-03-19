package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;

import java.time.LocalDate;
import java.util.List;

public interface HorarioMedicoService {
    List<HorarioMedicoEntity> obtenerHorariosPorMedico(Long idMedico);
    List<HorarioMedicoEntity> obtenerHorariosPorMedicoYDia(Long idMedico, HorarioMedicoEntity.DiaSemana diaSemana);
    List<CitaEntity> obtenerCitasReservadas(Long idMedico, LocalDate fecha);
}
