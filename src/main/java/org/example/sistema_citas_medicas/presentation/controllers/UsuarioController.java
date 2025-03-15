package org.example.sistema_citas_medicas.presentation.controllers;


import org.example.sistema_citas_medicas.data.entidades.Usuarios;
import org.example.sistema_citas_medicas.logic.dto.UsuarioDto;
import org.example.sistema_citas_medicas.logic.mappers.Mappers;
import org.example.sistema_citas_medicas.logic.servicios.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final Mappers<Usuarios, UsuarioDto> usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, Mappers<Usuarios, UsuarioDto> usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    // **Registro de usuario**
    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> registerUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuarios usuarioEntity = usuarioMapper.mapFrom(usuarioDto);
        Usuarios savedUsuario = usuarioService.save(usuarioEntity);
        UsuarioDto savedUsuarioDto = usuarioMapper.mapTo(savedUsuario);

        return new ResponseEntity<>(savedUsuarioDto, HttpStatus.CREATED);
    }

    // **Login de usuario**
    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@RequestParam Long id, @RequestParam String clave) {
        Optional<Usuarios> usuario = usuarioService.login(id, clave);

        return usuario.map(u -> {
            UsuarioDto usuarioDto = usuarioMapper.mapTo(u);
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    // **Obtener lista de usuarios**
    @GetMapping
    public List<UsuarioDto> listUsuarios() {
        return usuarioService.findAll()
                .stream()
                .map(usuarioMapper::mapTo)
                .collect(Collectors.toList());
    }

    // **Obtener un usuario por ID**
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable Long id) {
        Optional<Usuarios> usuario = usuarioService.findOne(id);

        return usuario.map(u -> {
            UsuarioDto usuarioDto = usuarioMapper.mapTo(u);
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // **Actualizar usuario completamente**
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> fullUpdateUsuario(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        if (!usuarioService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        usuarioDto.setId(id);
        Usuarios usuarioEntity = usuarioMapper.mapFrom(usuarioDto);
        Usuarios savedUsuario = usuarioService.save(usuarioEntity);
        UsuarioDto savedUsuarioDto = usuarioMapper.mapTo(savedUsuario);

        return new ResponseEntity<>(savedUsuarioDto, HttpStatus.OK);
    }

    // **Actualizar usuario parcialmente**
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDto> partialUpdateUsuario(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        if (!usuarioService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuarios usuarioEntity = usuarioMapper.mapFrom(usuarioDto);
        Usuarios updatedUsuario = usuarioService.partialUpdate(id, usuarioEntity);
        UsuarioDto updatedUsuarioDto = usuarioMapper.mapTo(updatedUsuario);

        return new ResponseEntity<>(updatedUsuarioDto, HttpStatus.OK);
    }

    // **Eliminar usuario**
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}