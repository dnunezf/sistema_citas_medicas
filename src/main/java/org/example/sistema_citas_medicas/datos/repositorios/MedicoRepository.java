package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {


    @Query("SELECT m FROM MedicoEntity m " +
            "WHERE (:especialidad IS NULL OR m.especialidad = :especialidad) " +
            "AND (:ubicacion IS NULL OR m.localidad = :ubicacion)")
    List<MedicoEntity> findByEspecialidadAndLocalidad(@Param("especialidad") String especialidad,
                                                      @Param("ubicacion") String ubicacion);


    // Obtener todas las especialidades únicas
    @Query("SELECT DISTINCT m.especialidad FROM MedicoEntity m")
    List<String> findDistinctEspecialidades();

    @Query("SELECT m FROM MedicoEntity m " +
            "WHERE (:especialidad IS NULL OR LOWER(m.especialidad) = LOWER(:especialidad)) " +
            "AND (:localidad IS NULL OR LOWER(m.localidad) LIKE %:localidad%)")
    List<MedicoEntity> buscarPorEspecialidadYLocalidad(@Param("especialidad") String especialidad,
                                                       @Param("localidad") String localidad);

}