package com.audiometria.audiometria.api.repository.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reporte_id_gen")
    @SequenceGenerator(name = "reporte_id_gen", sequenceName = "reporte_reporte_id_seq", allocationSize = 1)
    @Column(name = "reporte_id", nullable = false)
    private Integer id;

    @Column(name = "reporte_nombre", length = Integer.MAX_VALUE)
    private String reporteNombre;

    @Column(name = "reporte_estado", length = Integer.MAX_VALUE)
    private String reporteEstado;

    @NotNull
    @Column(name = "reporte_codigo", nullable = false, length = Integer.MAX_VALUE)
    private String reporteCodigo;

    @Column(name = "reporte_rol")
    private Long reporteRol;

    @Column(name = "reporte_apellido", length = Integer.MAX_VALUE)
    private String reporteApellido;

    @NotNull
    @Column(name = "reporte_cedula", nullable = false, length = Integer.MAX_VALUE)
    private String reporteCedula;

    @Column(name = "reporte_fecha_nacimiento")
    private LocalDate reporteFechaNacimiento;

    @Column(name = "reporte_fecha_creacion")
    private LocalDate reporteFechaCreacion;

    @Column(name = "reporte_fecha_modificacion")
    private LocalDate reporteFechaModificacion;

}