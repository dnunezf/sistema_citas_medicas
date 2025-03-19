package org.example.sistema_citas_medicas.logica.dto;

import java.time.LocalTime;

public class HorarioMedicoDto {
    private Long id;
    private Long idMedico;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int tiempoCita;
    private boolean reservado; // 🔥 Agregado para marcar citas ocupadas

    public HorarioMedicoDto() {}

    public HorarioMedicoDto(Long id, Long idMedico, String diaSemana, LocalTime horaInicio, LocalTime horaFin, int tiempoCita) {
        this.id = id;
        this.idMedico = idMedico;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tiempoCita = tiempoCita;
        this.reservado = false; // Por defecto, todas las citas estarán disponibles
    }

    // ✅ Nuevo constructor para espacios disponibles y reservados
    public HorarioMedicoDto(Long id, Long idMedico, String diaSemana, LocalTime horaInicio, LocalTime horaFin, int tiempoCita, boolean reservado) {
        this.id = id;
        this.idMedico = idMedico;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tiempoCita = tiempoCita;
        this.reservado = reservado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public int getTiempoCita() { return tiempoCita; }
    public void setTiempoCita(int tiempoCita) { this.tiempoCita = tiempoCita; }

    public boolean isReservado() { return reservado; }
    public void setReservado(boolean reservado) { this.reservado = reservado; } // 🔥 Agregado este método
}

