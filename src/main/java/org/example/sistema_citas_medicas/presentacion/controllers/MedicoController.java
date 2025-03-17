package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("medico", new MedicoDto());
        return "presentation/registro_medico";
    }

    @PostMapping("/registro")
    public String registrarMedico(@ModelAttribute MedicoDto medicoDTO, Model model) {
        try {
            medicoService.registrarMedico(medicoDTO);
            model.addAttribute("mensaje", "Médico registrado exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el médico: " + e.getMessage());
        }
        model.addAttribute("medico", new MedicoDto());
        return "presentation/registro_medico";
    }
}