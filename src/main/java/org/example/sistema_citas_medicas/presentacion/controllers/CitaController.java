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

    @GetMapping("/medico/{idMedico}/filtrar/estado")
    public String filtrarPorEstado(@PathVariable Long idMedico,
                                   @RequestParam("estado") String estado,
                                   Model model) {

        MedicoEntity medico = medicoService.obtenerPorId(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<CitaDto> citas;

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


    @PostMapping("/actualizar")
    public String actualizarCita(@RequestParam Long idCita, @RequestParam String estado, @RequestParam(required = false) String notas) {
        Long idMedico = citaService.obtenerIdMedicoPorCita(idCita);
        citaService.actualizarCita(idCita, CitaEntity.EstadoCita.valueOf(estado), notas);
        return "redirect:/citas/medico/" + idMedico;
    }


    @GetMapping("/ver")
    public String mostrarFormularioAgendar(Model model) {
        model.addAttribute("especialidades", medicoService.obtenerEspecialidades());
        return "presentation/agendar_cita";
    }

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

        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime fin = inicio.plusDays(7).withHour(23).withMinute(59);

        List<LocalDateTime> filtrados = espaciosDisponibles.stream()
                .filter(e -> !e.isBefore(inicio) && !e.isAfter(fin))
                .toList();

        Map<String, List<String>> espaciosPorFecha = new LinkedHashMap<>();
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter diaFormatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", new Locale("es", "ES"));

        for (LocalDateTime espacio : filtrados) {
            String clave = espacio.toLocalDate().format(diaFormatter);
            String horaFormateada = espacio.format(formatterHora);
            espaciosPorFecha.computeIfAbsent(clave, k -> new ArrayList<>()).add(horaFormateada);
        }

        model.addAttribute("medico", medico);
        model.addAttribute("horarios", horarios);
        model.addAttribute("espaciosPorFecha", espaciosPorFecha);
        model.addAttribute("idMedico", idMedico);

        return "presentation/seleccionar_horario";
    }

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
            cita.setFechaHora(LocalDateTime.parse(fechaHora));
            cita.setEstado(CitaEntity.EstadoCita.valueOf("pendiente"));

            citaService.guardarCita(cita);
            model.addAttribute("mensaje", "Cita agendada con éxito.");
        } else {
            model.addAttribute("error", "Error al agendar la cita, verifique los datos.");
        }

        return "redirect:/citas/mis_citas?idPaciente=" + idPaciente;
    }


    @GetMapping("/paciente/historico")
    public String verHistoricoCitas(HttpSession session, Model model) {
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != RolUsuario.PACIENTE) {
            return "redirect:/login";
        }

        List<CitaDto> citas = citaService.obtenerCitasPorPaciente(usuario.getId());

        Map<Long, MedicoEntity> medicosPorId = new HashMap<>();
        for (CitaDto cita : citas) {
            medicoService.obtenerPorId(cita.getIdMedico()).ifPresent(medico -> {
                medicosPorId.put(cita.getIdMedico(), medico);
            });
        }

        model.addAttribute("citas", citas);
        model.addAttribute("medicosPorId", medicosPorId);

        return "presentation/historico_citas";
    }

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
        return "presentation/historico_citas";
    } @GetMapping("/confirmar")
    public String confirmarCita(@RequestParam Long idMedico,
                                @RequestParam String fechaHora,
                                HttpSession session,
                                Model model) {

        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null) {
            session.setAttribute("urlPendiente", "/citas/confirmar?idMedico=" + idMedico + "&fechaHora=" + fechaHora);
            return "redirect:/usuarios/login";
        }

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

            if (medico.getRutaFotoPerfil() != null && medico.getRutaFotoPerfil().startsWith("/uploads/fotos_perfil/")) {
                String nombreArchivo = medico.getRutaFotoPerfil().replace("/uploads/fotos_perfil/", "");
                medico.setRutaFotoPerfil(nombreArchivo);
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

            String mensaje = "🩺 Cita confirmada exitosamente para el " +
                    fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);

            if (session.getAttribute("usuario") == null) {
                PacienteEntity paciente = pacienteService.obtenerPorId(idPaciente);
                session.setAttribute("usuario", paciente);
            }

            return "redirect:/";

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/horarios/" + idMedico;

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "❌ Error inesperado al confirmar la cita.");
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

    @GetMapping("/paciente/historico/filtrar")
    public String filtrarHistorico(@RequestParam(required = false) String estado,
                                   @RequestParam(required = false) String nombreMedico,
                                   HttpSession session,
                                   Model model) {
        UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != RolUsuario.PACIENTE) {
            return "redirect:/usuarios/login";
        }

        List<CitaDto> citas;

        boolean tieneEstado = estado != null && !estado.isBlank();
        boolean tieneNombre = nombreMedico != null && !nombreMedico.isBlank();

        if (tieneEstado && tieneNombre) {
            citas = citaService.filtrarCitasPorEstadoYNombreMedico(usuario.getId(), estado, nombreMedico);
        } else if (tieneEstado) {
            citas = citaService.filtrarCitasPorEstadoPaciente(usuario.getId(), CitaEntity.EstadoCita.valueOf(estado));
        } else if (tieneNombre) {
            citas = citaService.filtrarCitasPorNombreMedico(usuario.getId(), nombreMedico);
        } else {
            citas = citaService.obtenerCitasPorPaciente(usuario.getId());
        }

        Map<Long, MedicoEntity> medicosPorId = new HashMap<>();
        for (CitaDto cita : citas) {
            medicoService.obtenerPorId(cita.getIdMedico()).ifPresent(medico -> {
                medicosPorId.put(cita.getIdMedico(), medico);
            });
        }

        model.addAttribute("citas", citas);
        model.addAttribute("medicosPorId", medicosPorId);

        return "presentation/historico_citas";
    }







}



