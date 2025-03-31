package org.example.sistema_citas_medicas.seguridad;


import org.springframework.security.core.AuthenticationException;

public class MedicoNoAprobadoException extends AuthenticationException {
    public MedicoNoAprobadoException(String msg) {
        super(msg);
    }
}
