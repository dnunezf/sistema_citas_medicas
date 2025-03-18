package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
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

@Controller
@RequestMapping("/citas")
public class CitaController {
    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // Obtener todas las citas de un médico
    @GetMapping("/medico/{idMedico}")
    public String listarCitas(@PathVariable Long idMedico, Model model) {
        List<CitaDto> citas = citaService.obtenerCitasPorMedico(idMedico);
        model.addAttribute("citas", citas);
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