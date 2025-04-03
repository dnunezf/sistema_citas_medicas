package org.example.sistema_citas_medicas.presentacion.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;
import org.example.sistema_citas_medicas.datos.entidades.RolUsuario;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final Mapper<UsuarioEntity, UsuarioDto> usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, Mapper<UsuarioEntity, UsuarioDto> usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("login", new UsuarioDto());
        return "presentation/login/view";
    }






    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new UsuarioEntity());
        model.addAttribute("roles", getRolesDisponibles()); // Enviar los roles disponibles
        return "presentation/registro_usuario";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario,
                                   @RequestParam("confirmarClave") String confirmarClave,
                                   Model model) {
        try {
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                model.addAttribute("error", "El nombre no puede estar vacío.");
                return "presentation/registro_usuario";
            }

            if (!usuario.getClave().equals(confirmarClave)) {
                model.addAttribute("errorClave", "Las contraseñas no coinciden.");
                model.addAttribute("roles", getRolesDisponibles());
                return "presentation/registro_usuario";
            }

            // ✅ Validar si el ID ya está en uso
            if (usuarioService.findById(usuario.getId()).isPresent()) {
                model.addAttribute("error", "Ya existe un usuario registrado con ese número de identificación.");
                model.addAttribute("roles", getRolesDisponibles());
                return "presentation/registro_usuario";
            }

            // Registro según rol
            if (usuario.getRol() == RolUsuario.MEDICO) {
                MedicoEntity medico = new MedicoEntity(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getClave(),
                        "Especialidad no definida",
                        0.0,
                        "Localidad no especificada",
                        30,
                        "Presentación no disponible",
                        MedicoEntity.EstadoAprobacion.pendiente
                );
                usuarioService.save(medico);

            } else if (usuario.getRol() == RolUsuario.PACIENTE) {
                PacienteEntity paciente = new PacienteEntity(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getClave(),
                        LocalDate.of(2000, 1, 1),
                        "000-000-0000",
                        "Dirección no especificada"
                );
                usuarioService.save(paciente);

            } else {
                usuarioService.save(usuario);
            }

            model.addAttribute("mensaje", "Usuario registrado exitosamente.");
            return "redirect:/usuarios/login";

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            model.addAttribute("roles", getRolesDisponibles());
            return "presentation/registro_usuario";
        }
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }






    public RolUsuario[] getRolesDisponibles() {
        return RolUsuario.values(); // 🔥 Devuelve todos los valores del enum
    }

}


