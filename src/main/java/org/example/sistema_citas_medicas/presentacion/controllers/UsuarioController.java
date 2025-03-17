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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario, Model model) {
        try {
            if (usuario.getRol() == RolUsuario.MEDICO) {
                // Convertimos el usuario genérico en un MedicoEntity
                MedicoEntity medico = new MedicoEntity(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getClave(),
                        "Especialidad por defecto",  // Puedes cambiar esto con datos del formulario
                        0.0,
                        "Localidad por defecto",
                        30,
                        "Presentación por defecto",
                        MedicoEntity.EstadoAprobacion.PENDIENTE
                );
                usuarioService.save(medico); // 🔥 Pasamos el MedicoEntity en vez de UsuarioEntity
            } else {
                usuarioService.save(usuario); // Para otros tipos de usuario
            }
            model.addAttribute("mensaje", "Usuario registrado exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
        }
        return "presentation/registro_usuario"; // Ajusta esto según tu página de registro
    }


    public RolUsuario[] getRolesDisponibles() {
        return RolUsuario.values(); // 🔥 Devuelve todos los valores del enum
    }

}
