package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {

    private MedicoRepository medicoRepository;

    public MedicoServiceImpl(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    // CREATE (NO ID)
    @Override
    public MedicoEntity registrarMedico(MedicoEntity medicoEntity) {
        return medicoRepository.save(medicoEntity);
    }
}
