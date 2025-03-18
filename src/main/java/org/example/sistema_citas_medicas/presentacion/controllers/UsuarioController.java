package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.RolUsuario;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    // 🟢 Cargar la página del login
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("login", new UsuarioDto()); // Se usa para enlazar el formulario con el objeto
        return "presentation/login/view"; // Renderiza templates/presentation/login/view.html
    }

    // 🟢 Procesar el login
    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("login") UsuarioDto usuarioDto, Model model) {
        Optional<UsuarioEntity> usuario = usuarioService.login(usuarioDto.getId(), usuarioDto.getClave());

        if (usuario.isPresent()) {
            return "redirect:/dashboard"; // ✅ Solo redirige si el login es correcto
        } else {
            model.addAttribute("error", "⚠️ Usuario o contraseña incorrectos.");
            return "presentation/login/view"; // 🔄 Recarga la misma página con el mensaje de error
        }
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new UsuarioEntity());
        model.addAttribute("roles", getRolesDisponibles()); // Enviar los roles disponibles
        return "presentation/registro_usuario";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(
            @ModelAttribute("usuario") UsuarioEntity usuario,
            @RequestParam("confirmarClave") String confirmarClave,
            Model model) {
        try {
            // Asegurar que la lista de roles siempre esté presente en el modelo
            model.addAttribute("roles", getRolesDisponibles());

            // Validación: El nombre no puede estar vacío
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                model.addAttribute("error", "El nombre no puede estar vacío.");
                return "presentation/registro_usuario";
            }

            // Validación: La contraseña y la confirmación deben coincidir
            if (!usuario.getClave().equals(confirmarClave)) {
                model.addAttribute("errorClave", "Las contraseñas no coinciden.");
                return "presentation/registro_usuario";
            }

            // Si el usuario es médico, crear su entidad correspondiente
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
            } else {
                usuarioService.save(usuario);
            }

            model.addAttribute("mensaje", "Usuario registrado exitosamente.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "presentation/registro_usuario";
        }
    }

    public RolUsuario[] getRolesDisponibles() {
        return RolUsuario.values(); // 🔥 Devuelve todos los valores del enum
    }

}
