package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedicoEntity, Long> {

    @Query("SELECT h FROM HorarioMedicoEntity h WHERE h.medico.id = :idMedico")
    List<HorarioMedicoEntity> findByMedicoId(@Param("idMedico") Long idMedico);
}

