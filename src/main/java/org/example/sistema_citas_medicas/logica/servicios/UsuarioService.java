package org.example.sistema_citas_medicas.logica.servicios;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario);

    List<UsuarioEntity> findAll();

    Optional<UsuarioEntity> findOne(Long id);

    boolean isExists(Long id);

    UsuarioEntity partialUpdate(Long id, UsuarioEntity usuario);

    void delete(Long id);

    Optional<UsuarioEntity> login(Long id, String clave);

    Optional<UsuarioEntity> findById(Long id);

}
