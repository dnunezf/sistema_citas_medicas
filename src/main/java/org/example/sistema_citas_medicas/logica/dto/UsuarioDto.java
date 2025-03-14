package org.example.sistema_citas_medicas.logica.dto;

public class UsuarioDto
{
    private Long id;
    private String nombre;
    private String rol;

    public UsuarioDto() {}

    public UsuarioDto(Long id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
