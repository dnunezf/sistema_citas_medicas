package org.example.sistema_citas_medicas.seguridad;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import org.springframework.security.web.savedrequest.*;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;
    private final RequestCache requestCache = new HttpSessionRequestCache();

    public CustomSuccessHandler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String userIdStr = authentication.getName();
        Long userId = Long.parseLong(userIdStr);

        Optional<UsuarioEntity> usuarioOpt = usuarioService.findById(userId);

        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();
            request.getSession().setAttribute("usuario", usuario);

            // ✅ Intentar recuperar URL previa desde SavedRequest
            SavedRequest savedRequest = requestCache.getRequest(request, response);

            if (savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                response.sendRedirect(targetUrl);
                return;
            }

            // 🔁 Si no hay URL previa, redirigir según rol
            String rol = authentication.getAuthorities().iterator().next().getAuthority();

            if (rol.contains("ADMINISTRADOR")) {
                response.sendRedirect("/admin/lista");

            } else if (rol.contains("MEDICO")) {
                MedicoEntity medico = (MedicoEntity) usuario;
                boolean perfilIncompleto =
                        "Especialidad no definida".equals(medico.getEspecialidad()) ||
                                "Presentación no disponible".equals(medico.getPresentacion());

                if (perfilIncompleto) {
                    response.sendRedirect("/medicos/perfil/" + medico.getId());
                } else {
                    response.sendRedirect("/citas/medico/" + medico.getId());
                }

            } else {
                response.sendRedirect("/citas/paciente/historico");
            }
        } else {
            response.sendRedirect("/usuarios/login?error=true");
        }
    }
}


