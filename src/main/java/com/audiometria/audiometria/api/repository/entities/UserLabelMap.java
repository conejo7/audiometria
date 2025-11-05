package com.audiometria.audiometria.api.repository.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "user_label_map")
public class UserLabelMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "label_name", nullable = false, length = Integer.MAX_VALUE)
    private String labelName;

    @NotNull
    @Column(name = "user_name", nullable = false, length = Integer.MAX_VALUE)
    private String userName;

    @Column(name = "porcentaje", precision = 5, scale = 2)
    private BigDecimal porcentaje;


}