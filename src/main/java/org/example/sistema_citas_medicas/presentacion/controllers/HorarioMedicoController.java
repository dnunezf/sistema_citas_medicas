package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/horarios")
public class HorarioMedicoController {

    private final HorarioMedicoService horarioMedicoService;

    public HorarioMedicoController(HorarioMedicoService horarioMedicoService) {
        this.horarioMedicoService = horarioMedicoService;
    }

    @GetMapping("/medico/{idMedico}")
    public String listarHorarios(@PathVariable Long idMedico, Model model) {
        List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(idMedico);
        model.addAttribute("horarios", horarios);
        model.addAttribute("idMedico", idMedico);
        return "presentation/gestion_horarios";
    }

    @GetMapping("/nuevo/{idMedico}")
    public String mostrarFormularioNuevo(@PathVariable Long idMedico, Model model) {
        HorarioMedicoDto horarioDto = new HorarioMedicoDto();
        model.addAttribute("horario", horarioDto);
        model.addAttribute("idMedico", idMedico);
        return "presentation/form_horario";
    }

    @PostMapping("/guardar/{idMedico}")
    public String guardarHorario(@PathVariable Long idMedico, @ModelAttribute HorarioMedicoDto horarioDto, RedirectAttributes redirectAttributes) {
        if (horarioDto.getHoraInicio() == null || horarioDto.getHoraFin() == null) {
            redirectAttributes.addFlashAttribute("error", "Las horas de inicio y fin son obligatorias.");
            return "redirect:/horarios/nuevo/" + idMedico;
        }

        horarioDto.setIdMedico(idMedico);
        horarioMedicoService.guardarHorario(horarioDto);

        redirectAttributes.addFlashAttribute("mensaje", "Horario guardado correctamente.");
        return "redirect:/horarios/medico/" + idMedico;
    }

    @GetMapping("/editar/{idHorario}")
    public String mostrarFormularioEdicion(@PathVariable Long idHorario, Model model) {
        HorarioMedicoDto horario = horarioMedicoService.obtenerHorarioPorId(idHorario);
        model.addAttribute("horario", horario);
        model.addAttribute("idMedico", horario.getIdMedico());
        return "presentation/form_horario";
    }

    @PostMapping("/actualizar/{idHorario}")
    public String actualizarHorario(@PathVariable Long idHorario, @ModelAttribute HorarioMedicoDto horarioDto) {
        horarioMedicoService.actualizarHorario(idHorario, horarioDto);
        return "redirect:/horarios/medico/" + horarioDto.getIdMedico();
    }

    @PostMapping("/eliminar/{idHorario}")
    public String eliminarHorario(@PathVariable Long idHorario) {
        Long idMedico = horarioMedicoService.obtenerIdMedicoPorHorario(idHorario); // suponiendo que lo podés obtener
        horarioMedicoService.eliminarHorario(idHorario);
        return "redirect:/horarios/medico/" + idMedico;
    }

}

