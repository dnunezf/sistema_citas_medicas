package org.example.sistema_citas_medicas.data.repositories;

import org.example.sistema_citas_medicas.data.entidades.RolUsuario;
import org.example.sistema_citas_medicas.data.entidades.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {
    // Buscar usuario por ID (el ID es ingresado por el usuario en el login)
    Optional<Usuarios> findById(Long id);

    @Query("SELECT u FROM Usuarios u WHERE u.id = :id AND u.clave = :clave")
    Optional<Usuarios> findByIdAndClave(@Param("id") Long id, @Param("clave") String clave);

    // Buscar todos los usuarios de un rol específico
    @Query("SELECT u FROM Usuarios u WHERE u.rol = :rol")
    Optional<Usuarios> findByRol(@Param("rol") RolUsuario rol);
}
