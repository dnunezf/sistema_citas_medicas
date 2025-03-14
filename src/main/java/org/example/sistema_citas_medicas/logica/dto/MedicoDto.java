package org.example.sistema_citas_medicas.logica.dto;

public class MedicoDto extends UsuarioDto{
    private String especialidad;
    private Double costoConsulta;
    private String localidad;
    private int frecuenciaCitas;
    private String presentacion;
    private String estadoAprobacion;

    // Constructor vacío y con parámetros
    public MedicoDto() {}

    public MedicoDto(Long id, String nombre, String rol, String especialidad, Double costoConsulta, String localidad,
                     int frecuenciaCitas, String presentacion, String estadoAprobacion) {
        super(id, nombre, rol);
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

    public String getEstadoAprobacion() { return estadoAprobacion; }
    public void setEstadoAprobacion(String estadoAprobacion) { this.estadoAprobacion = estadoAprobacion; }
}
