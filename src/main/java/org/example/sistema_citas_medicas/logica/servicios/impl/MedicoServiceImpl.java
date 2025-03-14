package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.MedicoMapper;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {
    private final MedicoRepository medicoRepository;

    // Inyección de dependencia por constructor
    public MedicoServiceImpl(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Override
    public MedicoDto registrarMedico(MedicoDto medicoDTO) {

        MedicoEntity medico = medicoRepository.findById(medicoDTO.getId()).orElse(null);

        if (medico == null) {
            medico = MedicoMapper.toEntity(medicoDTO);
        } else {
            throw new IllegalArgumentException("El médico con ID " + medicoDTO.getId() + " ya está registrado.");
        }

        medico = medicoRepository.save(medico); // Ahora Hibernate lo manejará bien
        return MedicoMapper.toDTO(medico);
    }
}
