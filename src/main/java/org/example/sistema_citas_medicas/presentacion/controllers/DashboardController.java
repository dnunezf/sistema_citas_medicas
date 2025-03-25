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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final MedicoService medicoService;
    private final HorarioMedicoService horarioMedicoService;
    private final CitaService citaService;

    public DashboardController(MedicoService medicoService, HorarioMedicoService horarioMedicoService, CitaService citaService) {
        this.medicoService = medicoService;
        this.horarioMedicoService = horarioMedicoService;
        this.citaService = citaService;
    }

    @GetMapping("/")
    public String mostrarDashboard(Model model,
                                   @RequestParam(required = false) String especialidad,
                                   @RequestParam(required = false) String localidad) {

        List<MedicoDto> medicos = (especialidad != null || localidad != null)
                ? medicoService.buscarPorEspecialidadYUbicacion(especialidad, localidad)
                : medicoService.obtenerMedicos();

        Map<Long, List<LocalDateTime>> espaciosDisponibles = new HashMap<>();
        Map<Long, Map<LocalDate, List<LocalDateTime>>> espaciosAgrupadosPorFecha = new HashMap<>();

        for (MedicoDto medico : medicos) {
            List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(medico.getId());
            List<LocalDateTime> espacios = citaService.obtenerEspaciosDisponibles(medico.getId(), horarios);

            espaciosDisponibles.put(medico.getId(), espacios);

            Map<LocalDate, List<LocalDateTime>> agrupados = espacios.stream()
                    .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));
            espaciosAgrupadosPorFecha.put(medico.getId(), agrupados);
        }

        model.addAttribute("medicos", medicos);
        model.addAttribute("espaciosAgrupados", espaciosAgrupadosPorFecha);
        return "presentation/dashboard";
    }


}
