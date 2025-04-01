package org.example.sistema_citas_medicas.seguridad;
import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMessage = exception.getMessage();

        if ("MEDICO_PENDIENTE".equalsIgnoreCase(errorMessage)) {
            response.sendRedirect("/usuarios/login?error=pendiente");
        } else if ("MEDICO_RECHAZADO".equalsIgnoreCase(errorMessage)) {
            response.sendRedirect("/usuarios/login?error=rechazado");
        } else {
            response.sendRedirect("/usuarios/login?error=true");
        }
    }
}




