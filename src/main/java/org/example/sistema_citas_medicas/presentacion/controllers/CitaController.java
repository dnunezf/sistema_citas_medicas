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
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String filtrarPorEstado(@PathVariable Long idMedico,
                                   @RequestParam("estado") String estado,
                                   Model model) {

        MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<CitaDto> citas;

        // Manejo de filtro "ALL"
        if (estado.equalsIgnoreCase("ALL")) {
            citas = citaService.obtenerCitasPorMedico(idMedico);
        } else {
            CitaEntity.EstadoCita estadoEnum = CitaEntity.EstadoCita.valueOf(estado);
            citas = citaService.filtrarCitasPorEstado(idMedico, estadoEnum);
        }

        model.addAttribute("medico", medico);
        model.addAttribute("citas", citas);
        return "presentation/gestion_citas";
    }


    @GetMapping("/medico/{idMedico}/filtrar")
    public String filtrarCitas(@PathVariable Long idMedico,
                               @RequestParam(required = false) String estado,
                               @RequestParam(required = false) String nombrePaciente,
                               Model model) {
        MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<CitaDto> citas;

        boolean filtraEstado = estado != null && !estado.equalsIgnoreCase("ALL");
        boolean filtraNombre = nombrePaciente != null && !nombrePaciente.isBlank();

        if (filtraEstado && filtraNombre) {
            citas = citaService.filtrarCitasPorEstadoYNombre(idMedico,
                    CitaEntity.EstadoCita.valueOf(estado), nombrePaciente);
        } else if (filtraEstado) {
            citas = citaService.filtrarCitasPorEstado(idMedico, CitaEntity.EstadoCita.valueOf(estado));
        } else if (filtraNombre) {
            citas = citaService.filtrarCitasPorPaciente(idMedico, nombrePaciente);
        } else {
            citas = citaService.obtenerCitasPorMedico(idMedico);
        }

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
        Optional<MedicoEntity> medicoOpt = medicoService.obtenerPorId(idMedico);

        if (medicoOpt.isEmpty()) {
            model.addAttribute("error", "No se encontró el médico.");
            return "error";
        }

        MedicoEntity medico = medicoOpt.get();
        List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(idMedico);
        List<LocalDateTime> espaciosDisponibles = citaService.obtenerEspaciosDisponibles(idMedico, horarios);

        // 🔄 Mostrar hoy + próximos 7 días
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime fin = inicio.plusDays(7).withHour(23).withMinute(59);

        List<LocalDateTime> filtrados = espaciosDisponibles.stream()
                .filter(e -> !e.isBefore(inicio) && !e.isAfter(fin))
                .toList();

        // 📆 Agrupar por fecha en formato amigable
        Map<String, List<String>> espaciosPorFecha = new LinkedHashMap<>();
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter diaFormatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", new Locale("es", "ES"));

        for (LocalDateTime espacio : filtrados) {
            String clave = espacio.toLocalDate().format(diaFormatter); // ej: "miércoles 03/04/2025"
            String horaFormateada = espacio.format(formatterHora);
            espaciosPorFecha.computeIfAbsent(clave, k -> new ArrayList<>()).add(horaFormateada);
        }

        model.addAttribute("medico", medico);
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
    @GetMapping("/confirmar")
    public String confirmarCita(@RequestParam Long idMedico,
                                @RequestParam String fechaHora,
                                HttpSession session,
                                Model model) {
        System.out.println("📥 Entrando a /citas/confirmar");
        System.out.println("➡️ idMedico = " + idMedico);
        System.out.println("➡️ fechaHora = " + fechaHora);

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            // 🟢 Guardar la URL exacta para retornar luego del login
            session.setAttribute("urlPendiente", "/citas/confirmar?idMedico=" + idMedico + "&fechaHora=" + fechaHora);
            return "redirect:/usuarios/login";
        }

        // 🛡️ Validar que sea paciente
        if (usuario.getRol() != RolUsuario.PACIENTE) {
            System.out.println("⚠️ El usuario no es paciente. Redirigiendo.");
            return "redirect:/";
        }

        System.out.println("👤 Usuario autenticado: " + usuario.getId());

        try {
            PacienteEntity paciente = pacienteService.obtenerPorId(usuario.getId());
            if (paciente == null) {
                System.out.println("❌ Paciente no encontrado en base");
                model.addAttribute("error", "No se encontraron datos de paciente.");
                return "redirect:/citas/ver";
            }

            MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                    .orElseThrow(() -> new RuntimeException("❌ Médico no encontrado con ID: " + idMedico));

            LocalDateTime fechaConvertida;
            try {
                fechaConvertida = LocalDateTime.parse(fechaHora);
                System.out.println("🕒 Fecha convertida correctamente: " + fechaConvertida);
            } catch (Exception e) {
                System.out.println("❌ Error al convertir la fecha: " + fechaHora);
                e.printStackTrace();
                return "redirect:/citas/ver";
            }

            model.addAttribute("medico", medico);
            model.addAttribute("paciente", paciente);
            model.addAttribute("fechaHora", fechaConvertida);

            return "presentation/confirmar_cita";

        } catch (Exception e) {
            System.out.println("🔥 ERROR INTERNO:");
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @PostMapping("/confirmar")
    public String procesarConfirmacion(@RequestParam Long idMedico,
                                       @RequestParam Long idPaciente,
                                       @RequestParam String fechaHora,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime fecha = LocalDateTime.parse(fechaHora);
            citaService.agendarCita(idPaciente, idMedico, fecha);

            // Mensaje para mostrar en la siguiente vista
            String mensaje = "🩺 Cita confirmada exitosamente para el " +
                    fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);

            // Guardar al paciente en sesión si no estuviera
            if (session.getAttribute("usuario") == null) {
                PacienteEntity paciente = pacienteService.obtenerPorId(idPaciente);
                session.setAttribute("usuario", paciente); // solo si no se ha guardado aún
            }

            return "redirect:/citas/paciente/historico";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "❌ Ocurrió un error al confirmar la cita.");
            return "redirect:/citas/ver";
        }
    }

    @GetMapping("/paciente/detalle/{idCita}")
    public String verDetalleCita(@PathVariable Long idCita, HttpSession session, Model model) {
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != RolUsuario.PACIENTE) {
            return "redirect:/usuarios/login";
        }

        CitaDto cita = citaService.obtenerCitaPorId(idCita);
        if (cita == null) {
            model.addAttribute("error", "Cita no encontrada.");
            return "redirect:/citas/paciente/historico";
        }

        MedicoEntity medico = medicoService.obtenerPorId(cita.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        model.addAttribute("cita", cita);
        model.addAttribute("medico", medico);
        return "presentation/detalle_cita";
    }






}



