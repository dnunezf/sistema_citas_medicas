package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.CitaRepository;
import org.example.sistema_citas_medicas.datos.repositorios.HorarioMedicoRepository;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HorarioMedicoServiceImpl implements HorarioMedicoService {
    private final HorarioMedicoRepository horarioMedicoRepository;
    private final CitaRepository citaRepository;

    public HorarioMedicoServiceImpl(HorarioMedicoRepository horarioMedicoRepository, CitaRepository citaRepository) {
        this.horarioMedicoRepository = horarioMedicoRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    public List<HorarioMedicoEntity> obtenerHorariosPorMedico(Long idMedico) {
        return horarioMedicoRepository.findByMedicoId(idMedico);
    }

    @Override
    public List<HorarioMedicoEntity> obtenerHorariosPorMedicoYDia(Long idMedico, HorarioMedicoEntity.DiaSemana diaSemana) {
        return horarioMedicoRepository.findByMedicoAndDia(idMedico, diaSemana.name().toLowerCase());
    }


    @Override
    public List<CitaEntity> obtenerCitasReservadas(Long idMedico, LocalDate fecha) {
        return citaRepository.obtenerCitasReservadas(idMedico, fecha);
    }
}
