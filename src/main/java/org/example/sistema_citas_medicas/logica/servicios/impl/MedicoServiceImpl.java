package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.impl.MedicoMapper;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoServiceImpl implements MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoServiceImpl(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    public Optional<MedicoEntity> obtenerPorId(Long id) {
        return medicoRepository.findById(id);
    }


    @Transactional
    public MedicoEntity actualizarMedico(MedicoEntity medico) {
        MedicoEntity medicoExistente = medicoRepository.findById(medico.getId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // 🔥 Mantener valores existentes si los nuevos son nulos o vacíos
        medicoExistente.setNombre(medico.getNombre() != null ? medico.getNombre() : medicoExistente.getNombre());
        medicoExistente.setEspecialidad(medico.getEspecialidad() != null ? medico.getEspecialidad() : medicoExistente.getEspecialidad());
        medicoExistente.setCostoConsulta(medico.getCostoConsulta() != null ? medico.getCostoConsulta() : medicoExistente.getCostoConsulta());
        medicoExistente.setLocalidad(medico.getLocalidad() != null ? medico.getLocalidad() : medicoExistente.getLocalidad());
        medicoExistente.setFrecuenciaCitas(medico.getFrecuenciaCitas() != 0 ? medico.getFrecuenciaCitas() : medicoExistente.getFrecuenciaCitas());
        medicoExistente.setPresentacion(medico.getPresentacion() != null ? medico.getPresentacion() : medicoExistente.getPresentacion());
        medicoExistente.setEstadoAprobacion(medico.getEstadoAprobacion() != null ? medico.getEstadoAprobacion() : medicoExistente.getEstadoAprobacion());

        return medicoRepository.save(medicoExistente);
    }



    public List<MedicoEntity> obtenerTodosMedicos() {
        return medicoRepository.findAll();
    }

    // Actualizar estado de aprobación
    @Transactional
    public void actualizarEstadoAprobacion(Long id, MedicoEntity.EstadoAprobacion estado) {
        MedicoEntity medico = medicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        medico.setEstadoAprobacion(estado);
        medicoRepository.save(medico);
    }

    @Override
    public byte[] obtenerFotoPerfil(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoEntity::getFotoPerfil)
                .orElse(null);
    }



}
