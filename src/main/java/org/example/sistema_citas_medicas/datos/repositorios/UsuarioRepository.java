package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByNombre(String nombre);
}