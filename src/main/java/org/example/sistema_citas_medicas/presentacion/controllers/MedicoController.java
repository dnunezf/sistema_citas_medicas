package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public String actualizarMedico(@ModelAttribute("medico") MedicoEntity medico,
                                   @RequestParam(value = "fotoPerfil", required = false) MultipartFile file,
                                   Model model) {
        if (medico.getId() == null || medico.getId() <= 0) {
            model.addAttribute("error", "El ID del médico es inválido.");
            return "presentation/registro_medico";
        }

        Optional<MedicoEntity> medicoExistente = medicoService.obtenerPorId(medico.getId());

        if (medicoExistente.isPresent()) {
            MedicoEntity medicoActual = medicoExistente.get();

            medicoActual.setNombre(medico.getNombre());
            medicoActual.setEspecialidad(medico.getEspecialidad());
            medicoActual.setCostoConsulta(medico.getCostoConsulta());
            medicoActual.setLocalidad(medico.getLocalidad());
            medicoActual.setFrecuenciaCitas(medico.getFrecuenciaCitas());
            medicoActual.setPresentacion(medico.getPresentacion());

            // Si se subió una nueva imagen, guardarla
            if (file != null && !file.isEmpty()) {
                try {
                    String nombreArchivo = "medico_" + medico.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path rutaCarpeta = Paths.get("uploads/fotos_perfil");
                    Files.createDirectories(rutaCarpeta);

                    Path rutaCompleta = rutaCarpeta.resolve(nombreArchivo);
                    Files.copy(file.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

                    // Guardamos solo la ruta relativa para usar en HTML
                    String rutaRelativa = "/uploads/fotos_perfil/" + nombreArchivo;
                    medicoActual.setRutaFotoPerfil(rutaRelativa);
                } catch (IOException e) {
                    model.addAttribute("error", "Error al guardar la foto de perfil.");
                    return "presentation/registro_medico";
                }
            }

            medicoService.actualizarMedico(medicoActual);
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