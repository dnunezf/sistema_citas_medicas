package org.example.sistema_citas_medicas.datos.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@PrimaryKeyJoinColumn(name = "id")
public class PacienteEntity extends UsuarioEntity {

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Column(name = "direccion", nullable = false, columnDefinition = "TEXT")
    private String direccion;

    public PacienteEntity() {}

    public PacienteEntity(Long id, String nombre, String clave, LocalDate fechaNacimiento, String telefono, String direccion) {
        super(id, nombre, clave, RolUsuario.PACIENTE);
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}