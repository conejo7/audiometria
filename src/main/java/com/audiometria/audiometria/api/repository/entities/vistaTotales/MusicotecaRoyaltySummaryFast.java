package com.audiometria.audiometria.api.repository.entities.vistaTotales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "mv_musicoteca_royalty_summary")
public class MusicotecaRoyaltySummaryFast {

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
    private LocalDateTime dateUser;

    @Column(name = "total_royalty")
    private BigDecimal totalRoyalty;

    @Column(name = "ganancia_neta")
    private BigDecimal gananciaNeta;

    @Column(name = "estado_factura")
    private String estadoFactura;

}