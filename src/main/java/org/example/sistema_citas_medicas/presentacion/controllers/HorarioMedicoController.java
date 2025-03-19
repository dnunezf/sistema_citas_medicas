package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.CitaEntity;
import org.example.sistema_citas_medicas.datos.entidades.HorarioMedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/horarios")
public class HorarioMedicoController {

    private final HorarioMedicoService horarioMedicoService;

    public HorarioMedicoController(HorarioMedicoService horarioMedicoService) {
        this.horarioMedicoService = horarioMedicoService;
    }

    @GetMapping("/medico/{idMedico}")
    public String verHorarioExtendido(@PathVariable Long idMedico,
                                      @RequestParam(value = "fecha", required = false) LocalDate fecha,
                                      Model model) {
        if (fecha == null) {
            fecha = LocalDate.now();
        }

        HorarioMedicoEntity.DiaSemana diaSemanaEnum = obtenerDiaSemana(fecha);
        System.out.println("🔍 Fecha recibida: " + fecha);
        System.out.println("🔍 Día de la semana calculado: " + diaSemanaEnum);

        List<HorarioMedicoEntity> horarios = horarioMedicoService.obtenerHorariosPorMedicoYDia(idMedico, diaSemanaEnum);
        System.out.println("📝 Horarios obtenidos: " + horarios.size());

        List<CitaEntity> citasReservadas = horarioMedicoService.obtenerCitasReservadas(idMedico, fecha);
        System.out.println("📅 Citas reservadas en esa fecha: " + citasReservadas.size());

        List<HorarioMedicoDto> espaciosDisponibles = new ArrayList<>();

        for (HorarioMedicoEntity horario : horarios) {
            LocalTime inicio = horario.getHoraInicio();
            LocalTime fin = horario.getHoraFin();
            int intervalo = horario.getTiempoCita();

            while (inicio.plusMinutes(intervalo).isBefore(fin) || inicio.plusMinutes(intervalo).equals(fin)) {
                LocalDateTime horarioActual = LocalDateTime.of(fecha, inicio);
                boolean reservado = citasReservadas.stream().anyMatch(c -> c.getFechaHora().equals(horarioActual));

                HorarioMedicoDto dto = new HorarioMedicoDto(horario.getId(), idMedico, diaSemanaEnum.name(), inicio, inicio.plusMinutes(intervalo), horario.getTiempoCita());
                dto.setReservado(reservado);
                espaciosDisponibles.add(dto);

                inicio = inicio.plusMinutes(intervalo);
            }
        }

        System.out.println("✅ Espacios disponibles generados: " + espaciosDisponibles.size());

        model.addAttribute("horarios", espaciosDisponibles);
        model.addAttribute("fecha", fecha);
        model.addAttribute("idMedico", idMedico);

        return "presentation/horario_medico";
    }

    private HorarioMedicoEntity.DiaSemana obtenerDiaSemana(LocalDate fecha) {
        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> HorarioMedicoEntity.DiaSemana.lunes;
            case TUESDAY -> HorarioMedicoEntity.DiaSemana.martes;
            case WEDNESDAY -> HorarioMedicoEntity.DiaSemana.miercoles;
            case THURSDAY -> HorarioMedicoEntity.DiaSemana.jueves;
            case FRIDAY -> HorarioMedicoEntity.DiaSemana.viernes;
            case SATURDAY -> HorarioMedicoEntity.DiaSemana.sabado;
            case SUNDAY -> HorarioMedicoEntity.DiaSemana.domingo;
        };
    }
}
