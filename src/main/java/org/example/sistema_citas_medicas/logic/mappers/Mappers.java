package org.example.sistema_citas_medicas.logic.mappers;

import org.example.sistema_citas_medicas.data.entidades.Usuarios;
import org.example.sistema_citas_medicas.logic.dto.UsuarioDto;

public interface Mappers<A, B> {
    B mapTo(A a); // Convierte de Entidad a DTO
    A mapFrom(B b); // Convierte de DTO a Entidad
}

