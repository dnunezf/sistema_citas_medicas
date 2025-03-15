package org.example.sistema_citas_medicas.presentation.controllers;

import org.example.sistema_citas_medicas.data.entidades.Usuarios;
import org.example.sistema_citas_medicas.logic.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logic.mappers.Mappers;
import org.example.sistema_citas_medicas.logic.servicios.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final Mappers<Usuarios, UsuarioDto> usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, Mappers<Usuarios, UsuarioDto> usuarioMapper) {
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
        Optional<Usuarios> usuario = usuarioService.login(usuarioDto.getId(), usuarioDto.getClave());

        if (usuario.isPresent()) {
            return "redirect:/dashboard"; // ✅ Solo redirige si el login es correcto
        } else {
            model.addAttribute("error", "⚠️ Usuario o contraseña incorrectos.");
            return "presentation/login/view"; // 🔄 Recarga la misma página con el mensaje de error
        }
    }
}
