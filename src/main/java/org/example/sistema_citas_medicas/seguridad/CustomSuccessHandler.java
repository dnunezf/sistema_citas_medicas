package org.example.sistema_citas_medicas.seguridad;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomSuccessHandler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String userIdStr = authentication.getName();
        Long userId = Long.valueOf(userIdStr);

        Optional<UsuarioEntity> usuarioOpt = usuarioService.findById(userId);

        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();
            request.getSession().setAttribute("usuario", usuario);

            // Redirección según rol
            String rol = authentication.getAuthorities().iterator().next().getAuthority();

            if (rol.contains("ADMINISTRADOR")) {
                response.sendRedirect("/admin/lista");
            } else if (rol.contains("MEDICO")) {
                response.sendRedirect("/medicos/perfil/" + usuario.getId());
            } else {
                response.sendRedirect("/"); // o tu dashboard de paciente
            }
        } else {
            // Fallback por si el usuario no existe
            response.sendRedirect("/usuarios/login?error=true");
        }
    }
}



