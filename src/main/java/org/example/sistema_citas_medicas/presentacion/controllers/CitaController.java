package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/citas")
public class CitaController {
    private final CitaService citaService;
    private final MedicoService medicoService;

    public CitaController(CitaService citaService, MedicoService medicoService) {
        this.citaService = citaService;
        this.medicoService = medicoService;
    }

    @GetMapping("/medico/{idMedico}")
    public String listarCitas(@PathVariable Long idMedico, Model model) {
        // Verificar si el médico existe
        Optional<MedicoEntity> medico = medicoService.obtenerPorId(idMedico);
        if (medico.isEmpty()) {
            model.addAttribute("error", "El médico no existe.");
            return "presentation/gestion_citas";
        }

        // Obtener citas
        List<CitaDto> citas = citaService.obtenerCitasPorMedico(idMedico);
        if (citas.isEmpty()) {
            model.addAttribute("mensaje", "No hay citas registradas para este médico.");
        }

        model.addAttribute("citas", citas);
        model.addAttribute("medico", medico.get()); // ✅ Agregamos el médico para mostrar su nombre en la vista
        return "presentation/gestion_citas";
    }



    // Filtrar citas por estado
    @GetMapping("/medico/{idMedico}/filtrar/estado")
    public String filtrarPorEstado(@PathVariable Long idMedico, @RequestParam("estado") String estado, Model model) {
        List<CitaDto> citas = citaService.filtrarCitasPorEstado(idMedico, CitaEntity.EstadoCita.valueOf(estado));
        model.addAttribute("citas", citas);
        return "presentation/gestion_citas";
    }

    // Filtrar citas por nombre del paciente
    @GetMapping("/medico/{idMedico}/filtrar/paciente")
    public String filtrarPorPaciente(@PathVariable Long idMedico, @RequestParam("nombrePaciente") String nombrePaciente, Model model) {
        List<CitaDto> citas = citaService.filtrarCitasPorPaciente(idMedico, nombrePaciente);
        model.addAttribute("citas", citas);
        return "presentation/gestion_citas";
    }

    // Actualizar estado y agregar notas a una cita
    @PostMapping("/actualizar")
    public String actualizarCita(@RequestParam Long idCita, @RequestParam String estado, @RequestParam(required = false) String notas) {
        citaService.actualizarCita(idCita, CitaEntity.EstadoCita.valueOf(estado), notas);
        return "redirect:/citas/medico/" + idCita;
    }
}