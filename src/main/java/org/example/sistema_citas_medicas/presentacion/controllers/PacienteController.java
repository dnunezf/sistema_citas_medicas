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
            model.addAttribute("error", "Paciente no encontrado");
            return "redirect:/pacientes/lista"; // Redirigir si no existe el paciente
        }
        model.addAttribute("paciente", paciente);
        return "presentation/registro_paciente";
    }

    // Procesar la actualización de datos del paciente
    @PostMapping("/actualizar")

    public String actualizarPaciente(@ModelAttribute("paciente") PacienteEntity paciente, RedirectAttributes redirectAttributes) {
        System.out.println("📥 Actualizando paciente:");
        System.out.println("ID: " + paciente.getId());
        System.out.println("Nombre: " + paciente.getNombre());
        System.out.println("Clave: " + paciente.getClave());
        System.out.println("Fecha Nacimiento: " + paciente.getFechaNacimiento());
        System.out.println("Teléfono: " + paciente.getTelefono());
        System.out.println("Dirección: " + paciente.getDireccion());
        System.out.println("Rol: " + paciente.getRol());

        try {

            pacienteService.actualizarPaciente(paciente);
            redirectAttributes.addFlashAttribute("mensaje", "Datos actualizados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar datos: " + e.getMessage());
        }
        return "redirect:/";
    }
}
