package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario); // Guardar un usuario

    List<UsuarioEntity> findAll(); // Obtener todos los usuarios

    Optional<UsuarioEntity> findOne(Long id); // Buscar usuario por ID

    boolean isExists(Long id); // Verificar si un usuario existe

    UsuarioEntity partialUpdate(Long id, UsuarioEntity usuario); // Actualizar parcialmente un usuario

    void delete(Long id); // Eliminar usuario por ID

    Optional<UsuarioEntity> login(Long id, String clave); // Login
    Optional<UsuarioEntity> findById(Long id);

}
