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

    @GetMapping("/perfil/{id}")
    public String mostrarFormularioRegistro(@PathVariable Long id, Model model) {
        Optional<MedicoEntity> medicoOpt = medicoService.obtenerPorId(id);

        if (medicoOpt.isPresent()) {
            MedicoEntity medico = medicoOpt.get();

            boolean datosCompletados = medico.getEspecialidad() != null && !medico.getEspecialidad().equals("Especialidad no definida")
                    && medico.getCostoConsulta() != null && medico.getCostoConsulta() > 0
                    && medico.getLocalidad() != null && !medico.getLocalidad().equals("Localidad no especificada")
                    && medico.getPresentacion() != null && !medico.getPresentacion().equals("Presentación no disponible")
                    && medico.getRutaFotoPerfil() != null && !medico.getRutaFotoPerfil().isBlank();


            if (!datosCompletados) {
                medico.setEspecialidad(null);
                medico.setCostoConsulta(null);
                medico.setLocalidad(null);
                medico.setFrecuenciaCitas(0);
                medico.setPresentacion(null);
                medico.setRutaFotoPerfil(null);
            }

            model.addAttribute("medico", medico);
        } else {
            model.addAttribute("error", "No se encontró un médico con el ID proporcionado.");
            model.addAttribute("medico", new MedicoEntity());
        }

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

            if (file != null && !file.isEmpty()) {
                try {
                    String nombreArchivo = "medico_" + medico.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path rutaCarpeta = Paths.get("uploads/fotos_perfil");
                    Files.createDirectories(rutaCarpeta);

                    Path rutaCompleta = rutaCarpeta.resolve(nombreArchivo);
                    Files.copy(file.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

                    String rutaRelativa = "/uploads/fotos_perfil/" + nombreArchivo;
                    medicoActual.setRutaFotoPerfil(rutaRelativa);
                } catch (IOException e) {
                    model.addAttribute("error", "Error al guardar la foto de perfil.");
                    model.addAttribute("medico", medico);
                    return "presentation/registro_medico";
                }
            }

            medicoService.actualizarMedico(medicoActual);
            model.addAttribute("mensaje", "Información actualizada correctamente.");
            model.addAttribute("medico", medicoActual);
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
            model.addAttribute("medico", new MedicoEntity());
        }

        return "presentation/registro_medico";
    }
}
