package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;

import java.time.LocalDate;
import java.util.List;

public interface CitaService {

    // Obtener todas las citas de un médico ordenadas
    List<CitaDto> obtenerCitasPorMedico(Long idMedico);

    // Filtrar citas por estado
    List<CitaDto> filtrarCitasPorEstado(Long idMedico, CitaEntity.EstadoCita estado);

    // Filtrar citas por nombre del paciente
    List<CitaDto> filtrarCitasPorPaciente(Long idMedico, String nombrePaciente);

    // Actualizar estado y anotaciones de una cita
    CitaDto actualizarCita(Long idCita, CitaEntity.EstadoCita nuevoEstado, String notas);

    List<CitaEntity> obtenerCitasReservadas(Long idMedico, LocalDate fecha);

}