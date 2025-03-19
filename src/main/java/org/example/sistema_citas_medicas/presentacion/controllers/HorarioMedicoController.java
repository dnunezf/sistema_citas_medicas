package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.HorarioMedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.HorarioMedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/horarios")
public class HorarioMedicoController {

    private final HorarioMedicoService horarioMedicoService;

    public HorarioMedicoController(HorarioMedicoService horarioMedicoService) {
        this.horarioMedicoService = horarioMedicoService;
    }

    // Listar horarios de un médico
    @GetMapping("/medico/{idMedico}")
    public String listarHorarios(@PathVariable Long idMedico, Model model) {
        List<HorarioMedicoDto> horarios = horarioMedicoService.obtenerHorariosPorMedico(idMedico);
        model.addAttribute("horarios", horarios);
        model.addAttribute("idMedico", idMedico);
        return "presentation/gestion_horarios";
    }

    // Mostrar formulario para agregar un nuevo horario
    @GetMapping("/nuevo/{idMedico}")
    public String mostrarFormularioNuevo(@PathVariable Long idMedico, Model model) {
        HorarioMedicoDto horarioDto = new HorarioMedicoDto();
        model.addAttribute("horario", horarioDto);
        model.addAttribute("idMedico", idMedico);
        return "presentation/form_horario";
    }

    // Guardar un nuevo horario
    @PostMapping("/guardar/{idMedico}")
    public String guardarHorario(@PathVariable Long idMedico, @ModelAttribute HorarioMedicoDto horarioDto) {
        horarioDto.setIdMedico(idMedico);
        horarioMedicoService.guardarHorario(horarioDto);
        return "redirect:/horarios/medico/" + idMedico;
    }

    // Mostrar formulario para editar un horario
    @GetMapping("/editar/{idHorario}")
    public String mostrarFormularioEdicion(@PathVariable Long idHorario, Model model) {
        HorarioMedicoDto horario = horarioMedicoService.obtenerHorarioPorId(idHorario);
        model.addAttribute("horario", horario);
        return "presentation/form_horario";
    }

    // Actualizar un horario existente
    @PostMapping("/actualizar/{idHorario}")
    public String actualizarHorario(@PathVariable Long idHorario, @ModelAttribute HorarioMedicoDto horarioDto) {
        horarioMedicoService.actualizarHorario(idHorario, horarioDto);
        return "redirect:/horarios/medico/" + horarioDto.getIdMedico();
    }

    // Eliminar un horario
    @PostMapping("/eliminar/{idHorario}")
    public String eliminarHorario(@PathVariable Long idHorario) {
        horarioMedicoService.eliminarHorario(idHorario);
        return "redirect:/horarios";
    }
}

