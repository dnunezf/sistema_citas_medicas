package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {


    // Buscar médicos por especialidad y ubicación
    @Query("SELECT m FROM MedicoEntity m WHERE m.especialidad = :especialidad AND m.localidad = :ubicacion")
    List<MedicoEntity> findByEspecialidadAndLocalidad(@Param("especialidad") String especialidad, @Param("ubicacion") String ubicacion);

    // Obtener todas las especialidades únicas
    @Query("SELECT DISTINCT m.especialidad FROM MedicoEntity m")
    List<String> findDistinctEspecialidades();

}