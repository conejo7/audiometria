package com.audiometria.audiometria.api.repository.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_label_estado",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_name", "label_name", "anio", "mes"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLabelEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "label_name", nullable = false)
    private String labelName;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // 🔥 Opcional pero recomendado: setear fecha automática
    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.fechaActualizacion = LocalDateTime.now();
    }

}
