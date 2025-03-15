package org.example.sistema_citas_medicas.presentacion.controllers;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private MedicoService medicoService;

    private Mapper<MedicoEntity, MedicoDto> medicoMapper;

    public MedicoController(MedicoService medicoService, Mapper<MedicoEntity, MedicoDto> medicoMapper) {
        this.medicoService = medicoService;
        this.medicoMapper = medicoMapper;
    }

    @PostMapping(path = "/registro")
    public ResponseEntity<MedicoDto> registrarMedico(@RequestBody MedicoDto medico)
    {
        MedicoEntity medicoEntity = medicoMapper.mapFrom(medico);
        MedicoEntity savedMedicoEntity = medicoService.registrarMedico(medicoEntity);
        MedicoDto savedMedicoDto = medicoMapper.mapTo(savedMedicoEntity);

        return new ResponseEntity<>(savedMedicoDto, HttpStatus.CREATED);
    }
}