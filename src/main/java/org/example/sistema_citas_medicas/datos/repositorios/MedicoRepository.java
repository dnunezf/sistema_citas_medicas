package org.example.sistema_citas_medicas.datos.repositorios;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {
}