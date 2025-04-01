package org.example.sistema_citas_medicas.seguridad;



import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.datos.repositorios.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UsuarioRepository usuarioRepository,
                                        PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String idRaw = authentication.getName(); // ID como string
        String clave = authentication.getCredentials().toString();

        Long id;
        try {
            id = Long.parseLong(idRaw);
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("ID inválido");
        }

        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        if (!passwordEncoder.matches(clave, usuario.getClave())) {
            throw new BadCredentialsException("Clave incorrecta");
        }

        // ✅ Validar estado médico
        if (usuario instanceof MedicoEntity medico) {
            switch (medico.getEstadoAprobacion()) {
                case pendiente -> throw new BadCredentialsException("MEDICO_PENDIENTE");
                case rechazado -> throw new BadCredentialsException("MEDICO_RECHAZADO");
            }
        }


        return new UsernamePasswordAuthenticationToken(
                usuario.getId().toString(),
                usuario.getClave(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
