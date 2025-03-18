package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.repositorios.CitaRepository;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.mappers.impl.CitaMapper;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {
    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    public CitaServiceImpl(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    // Obtener todas las citas de un médico ordenadas
    @Override
    public List<CitaDto> obtenerCitasPorMedico(Long idMedico) {
        return citaRepository.findByMedicoOrdenadas(idMedico)
                .stream()
                .map(citaMapper::mapTo)
                .collect(Collectors.toList());
    }

    // Filtrar por estado
    @Override
    public List<CitaDto> filtrarCitasPorEstado(Long idMedico, CitaEntity.EstadoCita estado) {
        return citaRepository.findByMedicoAndEstado(idMedico, estado)
                .stream()
                .map(citaMapper::mapTo)
                .collect(Collectors.toList());
    }

    // Filtrar por nombre del paciente
    @Override
    public List<CitaDto> filtrarCitasPorPaciente(Long idMedico, String nombrePaciente) {
        return citaRepository.findByMedicoAndPaciente(idMedico, nombrePaciente)
                .stream()
                .map(citaMapper::mapTo)
                .collect(Collectors.toList());
    }

    // Actualizar estado y anotaciones de una cita
    @Override
    @Transactional
    public CitaDto actualizarCita(Long idCita, CitaEntity.EstadoCita nuevoEstado, String notas) {
        Optional<CitaEntity> citaOpt = citaRepository.findById(idCita);

        if (citaOpt.isPresent()) {
            CitaEntity cita = citaOpt.get();
            cita.setEstado(nuevoEstado);
            if (notas != null) {
                cita.setNotas(notas);
            }
            return citaMapper.mapTo(citaRepository.save(cita));
        } else {
            throw new RuntimeException("Cita no encontrada");
        }
    }
}