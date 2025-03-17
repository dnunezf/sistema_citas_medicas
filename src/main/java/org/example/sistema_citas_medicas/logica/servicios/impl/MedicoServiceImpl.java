package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.impl.MedicoMapper;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Service;

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
        return medicoRepository.findById(medico.getId()).map(medicoExistente -> {
            medicoExistente.setNombre(medico.getNombre());
            medicoExistente.setEspecialidad(medico.getEspecialidad());
            medicoExistente.setCostoConsulta(medico.getCostoConsulta());
            medicoExistente.setLocalidad(medico.getLocalidad());
            medicoExistente.setFrecuenciaCitas(medico.getFrecuenciaCitas());
            medicoExistente.setPresentacion(medico.getPresentacion());
            medicoExistente.setEstadoAprobacion(medico.getEstadoAprobacion());
            return medicoRepository.save(medicoExistente);
        }).orElse(null);
    }

}
