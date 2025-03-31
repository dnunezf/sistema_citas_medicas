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
        if ("MEDICO_PENDIENTE".equalsIgnoreCase(exception.getMessage())) {
            response.sendRedirect("/usuarios/login?error=pendiente");
        } else {
            response.sendRedirect("/usuarios/login?error=true");
        }
    }
}



