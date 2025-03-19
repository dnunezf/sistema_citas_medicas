package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;
import org.example.sistema_citas_medicas.datos.repositorios.CitaRepository;
import org.example.sistema_citas_medicas.datos.repositorios.HorarioMedicoRepository;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.datos.repositorios.PacienteRepository;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.impl.CitaMapper;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.example.sistema_citas_medicas.logica.servicios.PacienteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ModelMapper modelMapper;
;

    public CitaServiceImpl(CitaRepository citaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository , HorarioMedicoRepository horarioMedicoRepository, PacienteRepository pacienteRepository1, ModelMapper modelMapper ) {
        this.citaRepository = citaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository1;
        this.modelMapper = modelMapper;
    }

    // ✅ Obtener todas las citas de un médico ordenadas de más recientes a más antiguas
    @Override
    public List<CitaDto> obtenerCitasPorMedico(Long idMedico) {
        List<CitaEntity> citas = citaRepository.findByMedicoOrdenadas(idMedico);
        return citas.stream().map(cita -> modelMapper.map(cita, CitaDto.class)).collect(Collectors.toList());
    }

    // ✅ Filtrar citas por estado
    @Override
    public List<CitaDto> filtrarCitasPorEstado(Long idMedico, CitaEntity.EstadoCita estado) {
        List<CitaEntity> citas = citaRepository.findByMedicoAndEstado(idMedico, estado);
        return citas.stream().map(cita -> modelMapper.map(cita, CitaDto.class)).collect(Collectors.toList());
    }

    // ✅ Filtrar citas por paciente
    @Override
    public List<CitaDto> filtrarCitasPorPaciente(Long idMedico, String nombrePaciente) {
        List<CitaEntity> citas = citaRepository.findByMedicoAndPaciente(idMedico, nombrePaciente);
        return citas.stream().map(cita -> modelMapper.map(cita, CitaDto.class)).collect(Collectors.toList());
    }

    // ✅ Actualizar estado y notas de la cita
    @Override
    public CitaDto actualizarCita(Long idCita, CitaEntity.EstadoCita estado, String notas) {
        CitaEntity cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(estado);
        if (notas != null) {
            cita.setNotas(notas);
        }
        CitaEntity citaActualizada = citaRepository.save(cita);
        return modelMapper.map(citaActualizada, CitaDto.class);
    }


    // ✅ Obtener ID del médico desde una cita
    @Override
    public Long obtenerIdMedicoPorCita(Long idCita) {
        return citaRepository.findById(idCita)
                .map(cita -> cita.getMedico().getId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }





    @Override
    public List<LocalDateTime> obtenerEspaciosDisponibles(Long idMedico, List<HorarioMedicoDto> horarios) {
        List<LocalDateTime> espaciosDisponibles = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate fechaActual = ahora.toLocalDate();

        for (int i = 0; i < 3; i++) { // Buscar horarios para los próximos 3 días
            LocalDate fecha = fechaActual.plusDays(i);
            DayOfWeek diaSemana = fecha.getDayOfWeek();

            for (HorarioMedicoDto horario : horarios) {
                if (diaSemana.name().equalsIgnoreCase(horario.getDiaSemana())) {
                    LocalTime horaInicio = LocalTime.parse(horario.getHoraInicio());
                    LocalTime horaFin = LocalTime.parse(horario.getHoraFin());
                    int duracion = horario.getTiempoCita();

                    LocalDateTime espacio = LocalDateTime.of(fecha, horaInicio);
                    while (!espacio.toLocalTime().isAfter(horaFin.minusMinutes(duracion))) {
                        boolean ocupado = citaRepository.existsByMedicoAndFechaHora(medicoRepository.getReferenceById(idMedico), espacio);
                        if (!ocupado) {
                            espaciosDisponibles.add(espacio);
                        }
                        espacio = espacio.plusMinutes(duracion);
                    }
                }
            }
        }

        return espaciosDisponibles;
    }

    // ✅ Agendar cita validando disponibilidad
    @Override
    @Transactional
    public CitaDto agendarCita(Long idPaciente, Long idMedico, LocalDateTime fechaHora) {
        PacienteEntity paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        MedicoEntity medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Validar que el horario no esté ocupado
        boolean existeCita = citaRepository.existsByMedicoAndFechaHora(medico, fechaHora);
        if (existeCita) {
            throw new RuntimeException("El horario seleccionado ya está ocupado.");
        }

        // Crear y guardar la cita
        CitaEntity nuevaCita = new CitaEntity(paciente, medico, fechaHora, CitaEntity.EstadoCita.pendiente, "");
        nuevaCita = citaRepository.save(nuevaCita);

        return modelMapper.map(nuevaCita, CitaDto.class);
    }


}
