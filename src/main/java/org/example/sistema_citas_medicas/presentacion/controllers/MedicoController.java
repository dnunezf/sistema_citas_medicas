package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping("/registro")
    public MedicoDto registrarMedico(@RequestBody MedicoDto medicoDTO) {
        return medicoService.registrarMedico(medicoDTO);
    }
}