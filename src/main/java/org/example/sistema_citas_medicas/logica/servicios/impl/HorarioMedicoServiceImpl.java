package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.HorarioMedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioMedicoServiceImpl implements HorarioMedicoService {

    private final HorarioMedicoRepository horarioMedicoRepository;
    private final ModelMapper modelMapper;

    public HorarioMedicoServiceImpl(HorarioMedicoRepository horarioMedicoRepository, ModelMapper modelMapper) {
        this.horarioMedicoRepository = horarioMedicoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<HorarioMedicoDto> obtenerHorariosPorMedico(Long idMedico) {
        List<HorarioMedicoEntity> horarios = horarioMedicoRepository.findByMedicoId(idMedico);
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

    @Override
    public HorarioMedicoDto guardarHorario(HorarioMedicoDto horarioDto) {
        HorarioMedicoEntity horario = modelMapper.map(horarioDto, HorarioMedicoEntity.class);
        HorarioMedicoEntity guardado = horarioMedicoRepository.save(horario);
        return modelMapper.map(guardado, HorarioMedicoDto.class);
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

