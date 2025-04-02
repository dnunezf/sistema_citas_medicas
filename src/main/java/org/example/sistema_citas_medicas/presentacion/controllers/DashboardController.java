package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.CitaDto;
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

            // ✅ Filtrar solo médicos aprobados
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

            System.out.println("👨‍⚕️ Médico: " + medico.getNombre());
            System.out.println("📸 Ruta Foto: " + medico.getRutaFotoPerfil());
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
