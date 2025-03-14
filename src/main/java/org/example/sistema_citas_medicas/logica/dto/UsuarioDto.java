package org.example.sistema_citas_medicas.logica.dto;

public class UsuarioDto
{
    private Long id; // Para identificar usuarios en actualizaciones
    private String nombre;
    private String clave;
    private String rol; // Puede ser paciente, medico o administrador

    public UsuarioDto() {
    }

    public UsuarioDto(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }

    public UsuarioDto(Long id, String nombre, String clave, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
