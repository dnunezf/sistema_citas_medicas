package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;
import org.example.sistema_citas_medicas.datos.entidades.RolUsuario;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.datos.repositorios.PacienteRepository;
import org.example.sistema_citas_medicas.datos.repositorios.UsuarioRepository;
import org.example.sistema_citas_medicas.logica.servicios.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Autowired
    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public PacienteEntity obtenerPorId(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    @Override
    public void actualizarPaciente(PacienteEntity paciente) {
        PacienteEntity pacienteExistente = pacienteRepository.findById(paciente.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        pacienteExistente.setNombre(paciente.getNombre());
        pacienteExistente.setClave(paciente.getClave());
        pacienteExistente.setFechaNacimiento(paciente.getFechaNacimiento());
        pacienteExistente.setTelefono(paciente.getTelefono());
        pacienteExistente.setDireccion(paciente.getDireccion());

        pacienteRepository.save(pacienteExistente);
    }

    @Override
    public Optional<PacienteEntity> findOne(Long id) {
        return pacienteRepository.findById(id);
    }
}

