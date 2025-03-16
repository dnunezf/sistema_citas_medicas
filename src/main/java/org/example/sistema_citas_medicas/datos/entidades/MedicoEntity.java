package org.example.sistema_citas_medicas.datos.entidades;

import jakarta.persistence.*;

@Entity

@Table(name = "medicos")
public class MedicoEntity extends UsuarioEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAprobacion estadoAprobacion;

    public enum EstadoAprobacion {
        PENDIENTE, APROBADO, RECHAZADO
    }

    public MedicoEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public MedicoEntity(Long id, String nombre, String clave, Rol rol, String especialidad, Double costoConsulta, String localidad, int frecuenciaCitas, String presentacion, EstadoAprobacion estadoAprobacion) {
        super(id, nombre, clave, rol);
        this.especialidad = especialidad;
        this.costoConsulta = costoConsulta;
        this.localidad = localidad;
        this.frecuenciaCitas = frecuenciaCitas;
        this.presentacion = presentacion;
        this.estadoAprobacion = estadoAprobacion;
    }

    public MedicoEntity(Long id, String nombre, String clave, Rol rol) {
        super(id, nombre, clave, rol);
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Double getCostoConsulta() {
        return costoConsulta;
    }

    public void setCostoConsulta(Double costoConsulta) {
        this.costoConsulta = costoConsulta;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getFrecuenciaCitas() {
        return frecuenciaCitas;
    }

    public void setFrecuenciaCitas(int frecuenciaCitas) {
        this.frecuenciaCitas = frecuenciaCitas;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public EstadoAprobacion getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(EstadoAprobacion estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }
}
