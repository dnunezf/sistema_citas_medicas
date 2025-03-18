package org.example.sistema_citas_medicas.presentacion.controllers;


import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    private final MedicoService medicoService;

    public AdministradorController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // Página de gestión de médicos
    @GetMapping("/lista")
    public String listarMedicos(Model model) {
        List<MedicoEntity> medicos = medicoService.obtenerTodosMedicos();
        model.addAttribute("medicos", medicos);
        return "presentation/gestion_medicos"; // Nombre del template Thymeleaf
    }

    // Cambiar el estado de aprobación de un médico
    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@RequestParam Long id, @RequestParam String estadoAprobacion, RedirectAttributes redirectAttributes) {
        try {
            medicoService.actualizarEstadoAprobacion(id, MedicoEntity.EstadoAprobacion.valueOf(estadoAprobacion));
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el estado.");
        }
        return "redirect:/admin/lista";
    }
}
