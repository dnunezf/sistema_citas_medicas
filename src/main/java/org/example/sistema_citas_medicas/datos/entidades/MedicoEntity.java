package org.example.sistema_citas_medicas.datos.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "medicos")
@PrimaryKeyJoinColumn(name = "id")
public class MedicoEntity extends UsuarioEntity {

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(nullable = false)
    private Double costoConsulta;

    @Column(nullable = false, length = 100)
    private String localidad;

    @Column(nullable = false)
    private int frecuenciaCitas;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String presentacion;

    @Column(nullable = true, columnDefinition = "LONGBLOB")
    private byte[] fotoPerfil; // ✅ Guardar imagen como bytes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAprobacion estadoAprobacion;

    public enum EstadoAprobacion {
        pendiente, aprobado, rechazado
    }

    public MedicoEntity() {}

    public MedicoEntity(Long id, String nombre, String clave, String especialidad, Double costoConsulta,
                        String localidad, int frecuenciaCitas, String presentacion, EstadoAprobacion estadoAprobacion) {
        super(id, nombre, clave, RolUsuario.MEDICO);
        this.especialidad = especialidad;
        this.costoConsulta = costoConsulta;
        this.localidad = localidad;
        this.frecuenciaCitas = frecuenciaCitas;
        this.presentacion = presentacion;
        this.estadoAprobacion = estadoAprobacion;
    }

    // Getters y Setters
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public Double getCostoConsulta() { return costoConsulta; }
    public void setCostoConsulta(Double costoConsulta) { this.costoConsulta = costoConsulta; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public int getFrecuenciaCitas() { return frecuenciaCitas; }
    public void setFrecuenciaCitas(int frecuenciaCitas) { this.frecuenciaCitas = frecuenciaCitas; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public EstadoAprobacion getEstadoAprobacion() { return estadoAprobacion; }
    public void setEstadoAprobacion(EstadoAprobacion estadoAprobacion) { this.estadoAprobacion = estadoAprobacion; }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
