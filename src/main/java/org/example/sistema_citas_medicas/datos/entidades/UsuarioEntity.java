package org.example.sistema_citas_medicas.datos.entidades;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String clave;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    public UsuarioEntity() {
    }

    public UsuarioEntity(Long id, String nombre, String clave, RolUsuario rol) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rol = rol;
    }

    public enum RolUsuario {
        PACIENTE, MEDICO, ADMINISTRADOR
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

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
}
