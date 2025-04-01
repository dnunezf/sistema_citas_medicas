package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.CitaService;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
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
                                   @RequestParam(required = false) String localidad) {

        List<MedicoDto> medicos;

        // 🔍 Búsqueda flexible: por especialidad, localidad, ambos o ninguno
        if ((especialidad == null || especialidad.isBlank()) &&
                (localidad == null || localidad.isBlank())) {
            medicos = medicoService.obtenerMedicos();
        } else {
            medicos = medicoService.buscarPorEspecialidadYUbicacion(especialidad, localidad);
        }

        Map<Long, Map<LocalDate, List<LocalDateTime>>> espaciosAgrupadosPorFecha = new HashMap<>();

        for (MedicoDto medico : medicos) {
            List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(medico.getId());
            List<LocalDateTime> espacios = citaService.obtenerEspaciosDisponibles(medico.getId(), horarios);

            Map<LocalDate, List<LocalDateTime>> agrupados = espacios.stream()
                    .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));

            espaciosAgrupadosPorFecha.put(medico.getId(), agrupados);
        }

        model.addAttribute("medicos", medicos);
        model.addAttribute("espaciosAgrupados", espaciosAgrupadosPorFecha);
        model.addAttribute("especialidad", especialidad); // Para mantener los filtros en el formulario
        model.addAttribute("localidad", localidad);

        return "presentation/dashboard";
    }
}
