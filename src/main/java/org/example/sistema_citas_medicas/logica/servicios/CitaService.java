package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;

import java.time.LocalDateTime;
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

    Long obtenerIdMedicoPorCita(Long idCita);

    CitaDto agendarCita(Long idPaciente, Long idMedico, LocalDateTime fechaHora);


    // Obtener horarios disponibles para los próximos 3 días
    List<LocalDateTime> obtenerEspaciosDisponibles(Long idMedico, List<HorarioMedicoDto> horarios);

    void guardarCita(CitaEntity cita);

    //*PUNTO 9*//
    List<CitaDto> obtenerCitasPorPaciente(Long idPaciente);

    List<CitaDto> filtrarCitasPorEstadoPaciente(Long idPaciente, CitaEntity.EstadoCita estado);

    List<CitaDto> filtrarCitasPorNombreMedico(Long idPaciente, String nombreMedico);

    CitaDto obtenerCitaPorId(Long idCita);

    List<CitaDto> filtrarCitasPorEstadoYNombre(Long idMedico, CitaEntity.EstadoCita estado, String nombrePaciente);

    String normalizar(String texto);

}