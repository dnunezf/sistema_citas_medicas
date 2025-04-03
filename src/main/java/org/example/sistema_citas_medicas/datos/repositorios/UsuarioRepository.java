package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.RolUsuario;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findById(Long id);

    @Query("SELECT u FROM UsuarioEntity u WHERE u.id = :id AND u.clave = :clave")
    Optional<UsuarioEntity> findByIdAndClave(@Param("id") Long id, @Param("clave") String clave);

    @Query("SELECT u FROM UsuarioEntity u WHERE u.rol = :rol")
    Optional<UsuarioEntity> findByRol(@Param("rol") RolUsuario rol);
}
