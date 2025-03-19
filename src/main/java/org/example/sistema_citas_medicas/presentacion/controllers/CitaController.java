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

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final MedicoService medicoService;

    public CitaController(CitaService citaService, MedicoService medicoService) {
        this.citaService = citaService;
        this.medicoService = medicoService;
    }

    // ✅ Listar todas las citas de un médico
    @GetMapping("/medico/{idMedico}")
    public String listarCitas(@PathVariable Long idMedico, Model model) {
        MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (medico == null) {
            throw new RuntimeException("El médico no existe");
        }

        List<CitaDto> citas = citaService.obtenerCitasPorMedico(idMedico);

        model.addAttribute("medico", medico);
        model.addAttribute("citas", citas);
        return "presentation/gestion_citas";
    }

    // ✅ Filtrar citas por estado
    @GetMapping("/medico/{idMedico}/filtrar/estado")
    public String filtrarPorEstado(@PathVariable Long idMedico, @RequestParam("estado") String estado, Model model) {
        MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (medico == null) {
            throw new RuntimeException("El médico no existe");
        }

        List<CitaDto> citas = citaService.filtrarCitasPorEstado(idMedico, CitaEntity.EstadoCita.valueOf(estado));

        model.addAttribute("medico", medico);
        model.addAttribute("citas", citas);
        return "presentation/gestion_citas";
    }

    // ✅ Filtrar citas por paciente
    @GetMapping("/medico/{idMedico}/filtrar/paciente")
    public String filtrarPorPaciente(@PathVariable Long idMedico, @RequestParam("nombrePaciente") String nombrePaciente, Model model) {
        MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (medico == null) {
            throw new RuntimeException("El médico no existe");
        }

        List<CitaDto> citas = citaService.filtrarCitasPorPaciente(idMedico, nombrePaciente);

        model.addAttribute("medico", medico);
        model.addAttribute("citas", citas);
        return "presentation/gestion_citas";
    }

    // ✅ Actualizar estado de la cita y agregar notas
    @PostMapping("/actualizar")
    public String actualizarCita(@RequestParam Long idCita, @RequestParam String estado, @RequestParam(required = false) String notas) {
        Long idMedico = citaService.obtenerIdMedicoPorCita(idCita);
        citaService.actualizarCita(idCita, CitaEntity.EstadoCita.valueOf(estado), notas);
        return "redirect:/citas/medico/" + idMedico;
    }
}
