package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.datos.repositorios.UsuarioRepository;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuarioServiceImpl implements UsuarioService{
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // REGISTRAR USUARIO
    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    // OBTENER TODOS LOS USUARIOS
    @Override
    public List<UsuarioEntity> findAll() {
        return StreamSupport.stream(usuarioRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // BUSCAR USUARIO POR ID
    @Override
    public Optional<UsuarioEntity> findOne(Long id) {
        return usuarioRepository.findById(id);
    }

    // VERIFICAR SI EXISTE EL USUARIO
    @Override
    public boolean isExists(Long id) {
        return usuarioRepository.existsById(id);
    }

    // ACTUALIZAR PARCIALMENTE USUARIO
    @Override
    public UsuarioEntity partialUpdate(Long id, UsuarioEntity usuario) {
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
    public Optional<UsuarioEntity> login(Long id, String clave) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByIdAndClave(id, clave);

        if (usuario.isEmpty()) { // 🔥 Verifica si está vacío antes de hacer get()
            return Optional.empty(); // 🚀 Devuelve vacío en lugar de lanzar error
        }

        return usuario;
    }
}
