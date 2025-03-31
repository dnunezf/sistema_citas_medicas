package org.example.sistema_citas_medicas.logica.servicios.impl;

import jakarta.transaction.Transactional;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.RolUsuario;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.datos.repositorios.UsuarioRepository;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              MedicoRepository medicoRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.medicoRepository = medicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioEntity save(UsuarioEntity usuario) {
        // Encriptar la clave antes de guardar
        String claveEncriptada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(claveEncriptada);

        if (usuario instanceof MedicoEntity) {
            return medicoRepository.save((MedicoEntity) usuario);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return StreamSupport.stream(usuarioRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioEntity> findOne(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public UsuarioEntity partialUpdate(Long id, UsuarioEntity usuario) {
        return usuarioRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(usuario.getNombre()).ifPresent(existingUser::setNombre);
            Optional.ofNullable(usuario.getClave()).ifPresent(clave -> {
                String claveEncriptada = passwordEncoder.encode(clave);
                existingUser.setClave(claveEncriptada);
            });
            Optional.ofNullable(usuario.getRol()).ifPresent(existingUser::setRol);
            return usuarioRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado!"));
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<UsuarioEntity> login(Long id, String claveNoEncriptada) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();

            // Compara clave enviada con la encriptada
            if (passwordEncoder.matches(claveNoEncriptada, usuario.getClave())) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty(); // Si no coincide la clave
    }

    @Override
    public Optional<UsuarioEntity> findById(Long id) {
        return usuarioRepository.findById(id);
    }

}
