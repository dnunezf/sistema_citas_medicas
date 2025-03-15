package org.example.sistema_citas_medicas.logic.servicios;

import org.example.sistema_citas_medicas.data.entidades.Usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuarios save(Usuarios usuario); // Guardar un usuario

    List<Usuarios> findAll(); // Obtener todos los usuarios

    Optional<Usuarios> findOne(Long id); // Buscar usuario por ID

    boolean isExists(Long id); // Verificar si un usuario existe

    Usuarios partialUpdate(Long id, Usuarios usuario); // Actualizar parcialmente un usuario

    void delete(Long id); // Eliminar usuario por ID

    Optional<Usuarios> login(Long id, String clave);
}
