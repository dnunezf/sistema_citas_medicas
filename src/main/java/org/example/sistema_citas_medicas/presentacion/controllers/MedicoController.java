package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/perfil")
    public String mostrarFormularioRegistro(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            Optional<MedicoEntity> medicoOpt = medicoService.obtenerPorId(id);
            medicoOpt.ifPresentOrElse(
                    medico -> model.addAttribute("medico", medico),
                    () -> model.addAttribute("error", "No se encontró un médico con el ID ingresado.")
            );
        } else {
            model.addAttribute("medico", new MedicoEntity()); // Si no hay ID, crear un nuevo objeto vacío
        }

        return "presentation/registro_medico";
    }


    @PostMapping("/actualizar")
    public String actualizarMedico(@ModelAttribute("medico") MedicoEntity medico, Model model) {
        try {
            Optional<MedicoEntity> medicoExistenteOpt = medicoService.obtenerPorId(medico.getId());

            if (medicoExistenteOpt.isPresent()) {
                MedicoEntity medicoExistente = medicoExistenteOpt.get();

                // ✅ Actualizar los demás campos
                medicoExistente.setNombre(medico.getNombre());
                medicoExistente.setEspecialidad(medico.getEspecialidad());
                medicoExistente.setCostoConsulta(medico.getCostoConsulta());
                medicoExistente.setLocalidad(medico.getLocalidad());
                medicoExistente.setFrecuenciaCitas(medico.getFrecuenciaCitas());
                medicoExistente.setPresentacion(medico.getPresentacion());

                medicoService.actualizarMedico(medicoExistente);
                model.addAttribute("mensaje", "Información actualizada correctamente.");
            } else {
                model.addAttribute("error", "El médico no existe.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la información: " + e.getMessage());
            e.printStackTrace();
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

        return "editar_medico"; // Retorna al formulario
    }

}