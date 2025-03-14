package org.example.sistema_citas_medicas.logica.servicios.impl;

import org.example.sistema_citas_medicas.datos.entidades.MedicoEntity;
import org.example.sistema_citas_medicas.datos.entidades.UsuarioEntity;
import org.example.sistema_citas_medicas.datos.repositorios.MedicoRepository;
import org.example.sistema_citas_medicas.datos.repositorios.UsuarioRepository;
import org.example.sistema_citas_medicas.logica.dto.MedicoDto;
import org.example.sistema_citas_medicas.logica.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logica.mappers.Mapper;
import org.example.sistema_citas_medicas.logica.mappers.impl.MedicoMapperImpl;
import org.example.sistema_citas_medicas.logica.mappers.impl.UsuarioMapperImpl;
import org.example.sistema_citas_medicas.logica.servicios.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoServiceImpl implements MedicoService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UsuarioMapperImpl usuarioMapper;

    @Autowired
    private MedicoMapperImpl medicoMapper;


    @Transactional
    public MedicoEntity registrarMedico(MedicoDto medicoDto) {

        UsuarioEntity usuario = usuarioMapper.mapFrom(new UsuarioDto(medicoDto.getNombre(), medicoDto.getClave()));
        usuario.setRol(UsuarioEntity.RolUsuario.MEDICO); // Asignar el rol correctamente
        usuario = usuarioRepository.save(usuario);

        MedicoEntity medico = medicoMapper.mapFrom(medicoDto, usuario);
        return medicoRepository.save(medico);
    }
}
