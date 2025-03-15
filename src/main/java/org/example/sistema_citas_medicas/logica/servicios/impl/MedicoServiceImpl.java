package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.impl.MedicoMapper;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoServiceImpl(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Override
    @Transactional
    public MedicoDto registrarMedico(MedicoDto medicoDTO) {
        // Verifica si el médico ya existe en la BD
        if (medicoRepository.existsById(medicoDTO.getId())) {
            throw new IllegalArgumentException("El médico con ID " + medicoDTO.getId() + " ya está registrado.");
        }

        // Mapea DTO -> Entidad
        MedicoEntity medico = medicoMapper.mapFrom(medicoDTO);

        // Guarda el médico en la BD
        medico = medicoRepository.save(medico);

        // Mapea Entidad -> DTO
        return medicoMapper.mapTo(medico);
    }
}
