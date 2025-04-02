package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.PacienteEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.logica.servicios.PacienteService;
import org.example.sistema_citas_medicas.logica.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Cargar formulario de edición con los datos del paciente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        PacienteEntity paciente = pacienteService.obtenerPorId(id);
        if (paciente == null) {
            model.addAttribute("error", "Paciente no encontrado.");
            return "redirect:/";
        }

        paciente.setClave(""); // 🔐 Borrar la clave para que no aparezca
        model.addAttribute("paciente", paciente);
        return "presentation/registro_paciente";
    }

    // Procesar la actualización de datos del paciente
    @PostMapping("/actualizar")
    public String actualizarPaciente(@ModelAttribute("paciente") PacienteEntity paciente,
                                     @RequestParam("confirmarClave") String confirmarClave,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        try {
            if (!paciente.getClave().equals(confirmarClave)) {
                model.addAttribute("errorClave", "Las contraseñas no coinciden.");
                model.addAttribute("paciente", paciente); // Necesario para que Thymeleaf mantenga los datos
                return "presentation/registro_paciente";
            }

            pacienteService.actualizarPaciente(paciente);
            redirectAttributes.addFlashAttribute("mensaje", "Datos actualizados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar datos: " + e.getMessage());
        }
        return "redirect:/pacientes/editar/" + paciente.getId();
    }

}
