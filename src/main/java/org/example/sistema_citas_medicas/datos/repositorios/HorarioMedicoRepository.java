package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedicoEntity, Long> {

    // Obtener horarios de un médico
    List<HorarioMedicoEntity> findByMedicoId(Long idMedico);

    // Obtener horarios de un médico para un día específico
    @Query("SELECT h FROM HorarioMedicoEntity h WHERE h.medico.id = :idMedico AND LOWER(h.diaSemana) = LOWER(:diaSemana)")
    List<HorarioMedicoEntity> findByMedicoAndDia(@Param("idMedico") Long idMedico, @Param("diaSemana") String diaSemana);


}
