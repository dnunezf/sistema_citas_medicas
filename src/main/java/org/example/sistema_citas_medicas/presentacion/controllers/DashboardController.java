package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.CitaDto;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final MedicoService medicoService;
    private final HorarioMedicoService horarioMedicoService;
    private final CitaService citaService;

    public DashboardController(MedicoService medicoService,
                               HorarioMedicoService horarioMedicoService,
                               CitaService citaService) {
        this.medicoService = medicoService;
        this.horarioMedicoService = horarioMedicoService;
        this.citaService = citaService;
    }

    @GetMapping("/")
    public String mostrarDashboard(Model model,
                                   @RequestParam(required = false) String especialidad,
                                   @RequestParam(required = false) String localidad,
                                   Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean esPaciente = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_PACIENTE"));

            if (!esPaciente) {
                if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MEDICO"))) {
                    Long idMedico = Long.parseLong(authentication.getName());
                    return "redirect:/citas/medico/" + idMedico;
                }
                if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
                    return "redirect:/admin/lista";
                }
            }
        }

        List<MedicoDto> medicos;

        if ((especialidad == null || especialidad.isBlank()) &&
                (localidad == null || localidad.isBlank())) {
            medicos = medicoService.obtenerMedicos().stream()
                    .filter(m -> "aprobado".equalsIgnoreCase(m.getEstadoAprobacion()))
                    .collect(Collectors.toList());
        } else {
            medicos = medicoService.buscarPorEspecialidadYUbicacion(especialidad, localidad).stream()
                    .filter(m -> "aprobado".equalsIgnoreCase(m.getEstadoAprobacion()))
                    .collect(Collectors.toList());
        }

        Map<Long, Map<LocalDate, List<LocalDateTime>>> espaciosAgrupadosPorFecha = new HashMap<>();
        Map<Long, Set<LocalDateTime>> horasOcupadasPorMedico = new HashMap<>();

        for (MedicoDto medico : medicos) {
            List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(medico.getId());
            List<LocalDateTime> espacios = citaService.generarTodosLosEspacios(medico.getId(), horarios);

            Map<LocalDate, List<LocalDateTime>> agrupados = espacios.stream()
                    .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));

            espaciosAgrupadosPorFecha.put(medico.getId(), agrupados);

            List<CitaDto> citasAgendadas = citaService.obtenerCitasPorMedico(medico.getId());
            Set<LocalDateTime> horasOcupadas = citasAgendadas.stream()
                    .map(CitaDto::getFechaHora)
                    .collect(Collectors.toSet());

            horasOcupadasPorMedico.put(medico.getId(), horasOcupadas);
        }

        model.addAttribute("medicos", medicos);
        model.addAttribute("espaciosAgrupados", espaciosAgrupadosPorFecha);
        model.addAttribute("horasOcupadas", horasOcupadasPorMedico);
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("localidad", localidad);
        model.addAttribute("especialidades", medicoService.obtenerEspecialidades());

        return "presentation/dashboard";
    }

}
