package org.example.sistema_citas_medicas.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomFailureHandler authFailureHandler;
    private final CustomSuccessHandler successHandler;
    private final CustomRequestCache requestCache;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomFailureHandler authFailureHandler,
                          CustomSuccessHandler successHandler,
                          CustomRequestCache requestCache) {
        this.userDetailsService = userDetailsService;
        this.authFailureHandler = authFailureHandler;
        this.successHandler = successHandler;
        this.requestCache = requestCache;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .requestCache(cache -> cache.requestCache(requestCache)) // 🔥 Aquí se aplica la magia
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/usuarios/**", "/citas/ver", "/citas/buscar", "/citas/horarios/**").permitAll()
                        .requestMatchers("/pacientes/editar/**", "/pacientes/actualizar").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/citas/medico/**", "/horarios/**", "/medicos/**").hasRole("MEDICO")
                        .requestMatchers("/citas/paciente/**", "/pacientes/**").hasRole("PACIENTE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/usuarios/login")
                        .loginProcessingUrl("/usuarios/login")
                        .successHandler(successHandler)
                        .failureHandler(authFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}


