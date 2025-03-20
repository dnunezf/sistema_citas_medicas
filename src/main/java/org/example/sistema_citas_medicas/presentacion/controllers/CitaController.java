package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final MedicoService medicoService;
    private final HorarioMedicoService horarioMedicoService;

    public CitaController(CitaService citaService, MedicoService medicoService, HorarioMedicoService horarioMedicoService, HorarioMedicoService horarioMedicoService1) {
        this.citaService = citaService;
        this.medicoService = medicoService;
        this.horarioMedicoService = horarioMedicoService1;
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


    // ✅ Mostrar formulario de búsqueda de médicos
    @GetMapping("/agendar")
    public String mostrarFormularioAgendar(Model model) {
        model.addAttribute("especialidades", medicoService.obtenerEspecialidades());
        return "presentation/agendar_cita";
    }

    // ✅ Buscar médicos por especialidad y ubicación
    @GetMapping("/buscar")
    public String buscarMedicos(@RequestParam String especialidad, @RequestParam String ubicacion, Model model) {
        List<MedicoDto> medicos = medicoService.buscarPorEspecialidadYUbicacion(especialidad, ubicacion);
        model.addAttribute("medicos", medicos);
        model.addAttribute("especialidades", medicoService.obtenerEspecialidades());
        return "presentation/agendar_cita";
    }

    @GetMapping("/horarios/{idMedico}")
    public String mostrarHorarios(@PathVariable Long idMedico, Model model) {
        List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(idMedico);
        List<LocalDateTime> espaciosDisponibles = citaService.obtenerEspaciosDisponibles(idMedico, horarios);

        // Agrupar espacios disponibles por fecha para mejor visualización
        Map<LocalDate, List<LocalDateTime>> espaciosPorFecha = espaciosDisponibles.stream()
                .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));

        model.addAttribute("horarios", horarios);
        model.addAttribute("espaciosPorFecha", espaciosPorFecha);
        model.addAttribute("idMedico", idMedico);
        return "presentation/seleccionar_horario";
    }



    // ✅ Agendar cita
    @PostMapping("/crear")
    public String crearCita(@RequestParam Long idMedico, @RequestParam LocalDateTime fechaHora,
                            @RequestParam(required = false) Long idPaciente,
                            RedirectAttributes redirectAttributes) {

        if (idPaciente == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para agendar una cita.");
            return "redirect:/login";
        }

        try {
            citaService.agendarCita(idPaciente, idMedico, fechaHora);
            redirectAttributes.addFlashAttribute("mensaje", "Cita agendada exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo agendar la cita. " + e.getMessage());
        }

        return "redirect:/citas/agendar";
    }



}
