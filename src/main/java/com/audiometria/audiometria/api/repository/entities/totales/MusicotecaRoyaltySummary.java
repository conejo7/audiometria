package com.audiometria.audiometria.api.repository.entities.totales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "v_musicoteca_royalty_summary")
public class MusicotecaRoyaltySummary {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "product_label")
    private String productLabel;

    @Column(name = "distribucion")
    private BigDecimal distribucion;

    @Column(name = "euro")
    private BigDecimal euro;

    @Column(name = "date_user")
    private LocalDate dateUser;

    @Column(name = "total_royalty")
    private BigDecimal totalRoyalty;

    @Column(name = "ganancia_neta")
    private BigDecimal ganancia_neta;

}
