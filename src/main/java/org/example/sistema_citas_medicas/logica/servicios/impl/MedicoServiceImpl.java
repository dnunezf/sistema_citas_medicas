package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.dto.RegistroMedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.MedicoMapper;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public MedicoDto registrarMedico(RegistroMedicoDto medicoDTO) {
        MedicoEntity medico = MedicoMapper.toEntity(medicoDTO);
        medico.setClave(medico.getClave());
        medico = medicoRepository.save(medico);
        return MedicoMapper.toDTO(medico);
    }
}
