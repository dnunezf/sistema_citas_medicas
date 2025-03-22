package org.example.sistema_citas_medicas.presentacion.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.sistema_citas_medicas.datos.entidades.*;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.*;
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
    private final PacienteService pacienteService;

    public CitaController(CitaService citaService, MedicoService medicoService, HorarioMedicoService horarioMedicoService, HorarioMedicoService horarioMedicoService1, PacienteService pacienteService) {
        this.citaService = citaService;
        this.medicoService = medicoService;
        this.horarioMedicoService = horarioMedicoService1;
        this.pacienteService = pacienteService;
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
    @GetMapping("/ver")
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
        // 🔹 Buscar el médico en la base de datos
        Optional<MedicoEntity> medicoOpt = medicoService.obtenerPorId(idMedico);

        if (medicoOpt.isEmpty()) {
            model.addAttribute("error", "No se encontró el médico.");
            return "error"; // Redirige a una página de error si el médico no existe
        }

        MedicoEntity medico = medicoOpt.get(); // 🔹 Obtener el objeto MedicoEntity

        // 🔹 Obtener los horarios y espacios disponibles
        List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(idMedico);
        List<LocalDateTime> espaciosDisponibles = citaService.obtenerEspaciosDisponibles(idMedico, horarios);

        // 🔹 Agrupar los espacios disponibles por fecha
        Map<LocalDate, List<LocalDateTime>> espaciosPorFecha = espaciosDisponibles.stream()
                .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));

        // 🔹 Agregar datos al modelo para Thymeleaf
        model.addAttribute("medico", medico);  // ⚠️ Aquí se envía el médico con su nombre
        model.addAttribute("horarios", horarios);
        model.addAttribute("espaciosPorFecha", espaciosPorFecha);
        model.addAttribute("idMedico", idMedico);

        return "presentation/seleccionar_horario";
    }




    // ✅ Agendar cita
    @GetMapping("/agendar")
    public String agendarCita(@RequestParam Long idMedico,
                              @RequestParam String fechaHora,
                              @RequestParam Long idPaciente,
                              Model model) {
        Optional<PacienteEntity> pacienteOpt = pacienteService.findOne(idPaciente);
        Optional<MedicoEntity> medicoOpt = medicoService.obtenerPorId(idMedico);

        if (pacienteOpt.isPresent() && medicoOpt.isPresent()) {
            CitaEntity cita = new CitaEntity();
            cita.setMedico(medicoOpt.get());
            cita.setPaciente(pacienteOpt.get());
            cita.setFechaHora(LocalDateTime.parse(fechaHora)); // Convertir String a LocalDateTime
            cita.setEstado(CitaEntity.EstadoCita.valueOf("pendiente"));

            citaService.guardarCita(cita);
            model.addAttribute("mensaje", "Cita agendada con éxito.");
        } else {
            model.addAttribute("error", "Error al agendar la cita, verifique los datos.");
        }

        return "redirect:/citas/mis_citas?idPaciente=" + idPaciente;
    }

    /*PUNTO 9*/

    // Mostrar el historial de citas del paciente
    @GetMapping("/paciente/historico")
    public String verHistoricoCitas(HttpSession session, Model model) {
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != RolUsuario.PACIENTE) {
            return "redirect:/login";
        }

        List<CitaDto> citas = citaService.obtenerCitasPorPaciente(usuario.getId());
        model.addAttribute("citas", citas);
        return "presentation/historico_citas";
    }

    // Filtrar por estado
    @GetMapping("/paciente/historico/filtrar/estado")
    public String filtrarHistoricoPorEstado(HttpSession session,
                                            @RequestParam("estado") String estado,
                                            Model model) {
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != RolUsuario.PACIENTE) {
            return "redirect:/login";
        }

        List<CitaDto> citas = citaService.filtrarCitasPorEstadoPaciente(usuario.getId(), CitaEntity.EstadoCita.valueOf(estado));
        model.addAttribute("citas", citas);
        return "presentation/historico_citas";
    }

    // Filtrar por nombre del médico
    @GetMapping("/paciente/historico/filtrar/medico")
    public String filtrarHistoricoPorMedico(HttpSession session,
                                            @RequestParam("nombreMedico") String nombreMedico,
                                            Model model) {
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != RolUsuario.PACIENTE) {
            return "redirect:/login";
        }

        List<CitaDto> citas = citaService.filtrarCitasPorNombreMedico(usuario.getId(), nombreMedico);
        model.addAttribute("citas", citas);
        return "presentation/historico_citas"; // Página aún por crear
    }
}



