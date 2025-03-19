package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.HorarioMedicoRepository;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioMedicoServiceImpl implements HorarioMedicoService {

    private final HorarioMedicoRepository horarioMedicoRepository;
    private final MedicoRepository medicoRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // ⏰ Formato 24 horas


    @Autowired
    private final ModelMapper modelMapper;

    public HorarioMedicoServiceImpl(HorarioMedicoRepository horarioMedicoRepository, MedicoRepository medicoRepository, ModelMapper modelMapper) {
        this.horarioMedicoRepository = horarioMedicoRepository;
        this.medicoRepository = medicoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<HorarioMedicoDto> obtenerHorariosPorMedico(Long idMedico) {
        List<HorarioMedicoEntity> horarios = horarioMedicoRepository.findByMedicoId(idMedico);

        if (horarios == null || horarios.isEmpty()) {
            return new ArrayList<>(); // Retorna lista vacía en vez de `null`
        }

        return horarios.stream()
                .map(horario -> modelMapper.map(horario, HorarioMedicoDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public HorarioMedicoDto obtenerHorarioPorId(Long idHorario) {
        HorarioMedicoEntity horario = horarioMedicoRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        return modelMapper.map(horario, HorarioMedicoDto.class);
    }

    public HorarioMedicoEntity guardarHorario(HorarioMedicoDto horarioDto) {
        HorarioMedicoEntity horario = new HorarioMedicoEntity();

        horario.setDiaSemana(HorarioMedicoEntity.DiaSemana.valueOf(horarioDto.getDiaSemana().toLowerCase())); // ✅ Convierte String a ENUM

        // Formato de 24 horas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        horario.setHoraInicio(LocalTime.parse(horarioDto.getHoraInicio(), formatter)); // ✅ Convierte String a LocalTime
        horario.setHoraFin(LocalTime.parse(horarioDto.getHoraFin(), formatter)); // ✅ Convierte String a LocalTime

        horario.setTiempoCita(horarioDto.getTiempoCita()); // ✅ Convierte String a int


        MedicoEntity medico = medicoRepository.findById(horarioDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Error: Médico no encontrado con ID " + horarioDto.getIdMedico()));

        horario.setMedico(medico); // Ahora sí se guarda correctamente
        return horarioMedicoRepository.save(horario);
    }


    @Override
    public HorarioMedicoDto actualizarHorario(Long idHorario, HorarioMedicoDto horarioDto) {
        return horarioMedicoRepository.findById(idHorario).map(horarioExistente -> {
            try {
                // Convertimos el valor del DTO a ENUM
                HorarioMedicoEntity.DiaSemana diaSemanaEnum = HorarioMedicoEntity.DiaSemana.valueOf(horarioDto.getDiaSemana().toUpperCase());
                horarioExistente.setDiaSemana(diaSemanaEnum);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Día de la semana inválido: " + horarioDto.getDiaSemana());
            }

            horarioExistente.setHoraInicio(LocalTime.parse(horarioDto.getHoraInicio()));
            horarioExistente.setHoraFin(LocalTime.parse(horarioDto.getHoraFin()));
            horarioExistente.setTiempoCita(horarioDto.getTiempoCita());
            HorarioMedicoEntity actualizado = horarioMedicoRepository.save(horarioExistente);
            return modelMapper.map(actualizado, HorarioMedicoDto.class);
        }).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
    }

    @Override
    public void eliminarHorario(Long idHorario) {
        if (!horarioMedicoRepository.existsById(idHorario)) {
            throw new RuntimeException("Horario no encontrado");
        }
        horarioMedicoRepository.deleteById(idHorario);
    }



}

