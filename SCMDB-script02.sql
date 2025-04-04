CREATE DATABASE SCMDB;

USE SCMDB;

CREATE TABLE usuarios(
    id BIGINT,
    nombre VARCHAR(100) NOT NULL,
    clave VARCHAR(100) NOT NULL,
    rol ENUM('PACIENTE', 'MEDICO', 'ADMINISTRADOR') NOT NULL,

    CONSTRAINT pk_usuarios PRIMARY KEY(id)
);

CREATE TABLE medicos(
    id BIGINT,

    especialidad VARCHAR(100) NOT  NULL,
    costo_consulta DECIMAL(10,2) NOT NULL,
    localidad VARCHAR(100) NOT NULL,
    frecuencia_citas INT NOT NULL,
    presentacion TEXT NOT NULL,
    estado_aprobacion ENUM('pendiente', 'aprobado', 'rechazado') NOT NULL,

    CONSTRAINT pf_medicos PRIMARY KEY(id),
    CONSTRAINT fk1_medicos FOREIGN KEY(id) REFERENCES usuarios(id)
);

CREATE TABLE horario_Medico(
    codigo_horario BIGINT AUTO_INCREMENT,
    id_medico BIGINT,
    dia_semana ENUM('lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    tiempo_cita INT NOT NULL,

    CONSTRAINT pk_horarioMedico PRIMARY KEY(codigo_horario),
    CONSTRAINT fk1_horarioMedico FOREIGN KEY(id_medico) REFERENCES medicos(id)
);

CREATE TABLE paciente(
    id BIGINT,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    direccion TEXT NOT NULL,

    CONSTRAINT pk_paciente PRIMARY KEY(id),
    CONSTRAINT fk1_paciente FOREIGN KEY(id) REFERENCES usuarios(id)
);

CREATE TABLE cita(
    codigo_cita BIGINT auto_increment,
    paciente BIGINT NOT NULL,
    medico BIGINT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('pendiente', 'confirmada', 'cancelada', 'completada') NOT NULL, 
    notas TEXT NULL,

    CONSTRAINT pk_cita PRIMARY KEY(codigo_cita),
    CONSTRAINT fk1_cita FOREIGN KEY(paciente) REFERENCES paciente(id),
    CONSTRAINT fk2_cita FOREIGN KEY(medico) REFERENCES medicos(id)
);