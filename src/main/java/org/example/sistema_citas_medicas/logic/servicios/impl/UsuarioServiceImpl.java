package org.example.sistema_citas_medicas.logic.servicios.impl;

import org.example.sistema_citas_medicas.data.entidades.Usuarios;
import org.example.sistema_citas_medicas.data.repositories.UsuarioRepository;
import org.example.sistema_citas_medicas.logic.servicios.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // REGISTRAR USUARIO
    @Override
    public Usuarios save(Usuarios usuario) {
        return usuarioRepository.save(usuario);
    }

    // OBTENER TODOS LOS USUARIOS
    @Override
    public List<Usuarios> findAll() {
        return StreamSupport.stream(usuarioRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // BUSCAR USUARIO POR ID
    @Override
    public Optional<Usuarios> findOne(Long id) {
        return usuarioRepository.findById(id);
    }

    // VERIFICAR SI EXISTE EL USUARIO
    @Override
    public boolean isExists(Long id) {
        return usuarioRepository.existsById(id);
    }

    // ACTUALIZAR PARCIALMENTE USUARIO
    @Override
    public Usuarios partialUpdate(Long id, Usuarios usuario) {
        return usuarioRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(usuario.getNombre()).ifPresent(existingUser::setNombre);
            Optional.ofNullable(usuario.getClave()).ifPresent(existingUser::setClave);
            Optional.ofNullable(usuario.getRol()).ifPresent(existingUser::setRol);
            return usuarioRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado!"));
    }

    // ELIMINAR USUARIO
    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    // MÉTODO PARA LOGIN
    @Override
    public Optional<Usuarios> login(Long id, String clave) {
        Optional<Usuarios> usuario = usuarioRepository.findByIdAndClave(id, clave);

        if (usuario.isEmpty()) { // 🔥 Verifica si está vacío antes de hacer get()
            return Optional.empty(); // 🚀 Devuelve vacío en lugar de lanzar error
        }

        return usuario;
    }
}
