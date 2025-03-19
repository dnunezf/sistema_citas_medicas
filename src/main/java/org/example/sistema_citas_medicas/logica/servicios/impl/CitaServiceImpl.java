package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.repositorios.CitaRepository;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.mappers.impl.CitaMapper;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final ModelMapper modelMapper;

    public CitaServiceImpl(CitaRepository citaRepository, MedicoRepository medicoRepository, ModelMapper modelMapper) {
        this.citaRepository = citaRepository;
        this.medicoRepository = medicoRepository;
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
}
