package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/perfil")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("medico", new MedicoDto());
        return "presentation/registro_medico";
    }

    @PostMapping("/actualizar")
    public String actualizarMedico(@ModelAttribute("medico") MedicoEntity medico, Model model) {
        System.out.println("🟢 ID recibido: " + medico.getId());

        if (medico.getId() == null || medico.getId() <= 0) {
            model.addAttribute("error", "El ID del médico es inválido.");
            return "presentation/registro_medico";
        }

        Optional<MedicoEntity> medicoExistente = medicoService.obtenerPorId(medico.getId());

        if (medicoExistente.isPresent()) {
            medicoService.actualizarMedico(medico);
            model.addAttribute("mensaje", "Información actualizada correctamente.");
        } else {
            model.addAttribute("error", "El médico no existe.");
        }

        return "presentation/registro_medico";
    }



    @GetMapping("/cargar")
    public String cargarMedico(@RequestParam("id") Long id, Model model) {
        Optional<MedicoEntity> medico = medicoService.obtenerPorId(id);

        if (medico.isPresent()) {
            model.addAttribute("medico", medico.get());
        } else {
            model.addAttribute("error", "No se encontró un médico con el ID ingresado.");
        }

        return "presentation/registro_medico"; // Retorna al formulario
    }


}