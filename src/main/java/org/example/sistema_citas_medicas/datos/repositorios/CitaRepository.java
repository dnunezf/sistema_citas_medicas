package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitaRepository extends JpaRepository<CitaEntity, Long> {

    // Obtener todas las citas de un médico, ordenadas de más recientes a más antiguas
    @Query("SELECT c FROM CitaEntity c WHERE c.medico.id = :idMedico ORDER BY c.fechaHora DESC")
    List<CitaEntity> findByMedicoOrdenadas(@Param("idMedico") Long idMedico);

    // Filtrar citas por estado
    @Query("SELECT c FROM CitaEntity c WHERE c.medico.id = :idMedico AND c.estado = :estado ORDER BY c.fechaHora DESC")
    List<CitaEntity> findByMedicoAndEstado(@Param("idMedico") Long idMedico, @Param("estado") CitaEntity.EstadoCita estado);

    // Filtrar citas por nombre del paciente
    @Query("SELECT c FROM CitaEntity c WHERE c.medico.id = :idMedico AND c.paciente.nombre LIKE %:nombrePaciente% ORDER BY c.fechaHora DESC")
    List<CitaEntity> findByMedicoAndPaciente(@Param("idMedico") Long idMedico, @Param("nombrePaciente") String nombrePaciente);
}