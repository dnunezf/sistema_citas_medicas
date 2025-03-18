package org.example.sistema_citas_medicas.presentacion.controllers;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            // Verificar que el nombre no sea nulo o vacío
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                model.addAttribute("error", "El nombre no puede estar vacío.");
                return "presentation/registro_usuario"; // Retorna la vista con el mensaje de error
            }

            if (usuario.getRol() == RolUsuario.MEDICO) {
                // Crear MedicoEntity asegurando valores válidos
                MedicoEntity medico = new MedicoEntity(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getClave(),
                        "Especialidad no definida",  // Especialidad por defecto
                        0.0,                         // Costo consulta predeterminado
                        "Localidad no especificada", // Localidad por defecto
                        30,                          // Frecuencia de citas por defecto
                        "Presentación no disponible",// Presentación por defecto
                        MedicoEntity.EstadoAprobacion.pendiente // Estado inicial
                );
                usuarioService.save(medico); // Guardar como Médico

            } else if (usuario.getRol() == RolUsuario.PACIENTE) {
                // Crear PacienteEntity con valores por defecto
                PacienteEntity paciente = new PacienteEntity(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getClave(),
                        LocalDate.of(2000, 1, 1), // Fecha de nacimiento por defecto
                        "000-000-0000",           // Teléfono por defecto
                        "Dirección no especificada" // Dirección por defecto
                );
                usuarioService.save(paciente); // Guardar como Paciente

            } else {
                usuarioService.save(usuario); // Guardar como usuario común
            }

            model.addAttribute("mensaje", "Usuario registrado exitosamente.");
            return "redirect:/login"; // Redirigir tras registro exitoso

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "presentation/registro_usuario"; // Volver al formulario en caso de error
        }
    }




    public RolUsuario[] getRolesDisponibles() {
        return RolUsuario.values(); // 🔥 Devuelve todos los valores del enum
    }

}


