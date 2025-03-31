package org.example.sistema_citas_medicas;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof PendingApprovalException) {
            response.sendRedirect("/usuarios/login?pendiente=true");
        } else if (exception instanceof BadCredentialsException) {
            response.sendRedirect("/usuarios/login?error=true");
        } else {
            response.sendRedirect("/usuarios/login?error=true");
        }
    }
}
