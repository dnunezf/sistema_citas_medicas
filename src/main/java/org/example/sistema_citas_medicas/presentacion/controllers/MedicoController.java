package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("medicoDTO", new MedicoDto());
        return "registro-medico";
    }

    @PostMapping("/registro")
    public String registrarMedico(@ModelAttribute MedicoDto medicoDTO, Model model) {
        medicoService.registrarMedico(medicoDTO);
        model.addAttribute("mensaje", "Registro exitoso. Esperando aprobación del administrador.");
        return "registro-exitoso";
    }
}