package com.audiometria.audiometria.api.repository.entities.estadisticas;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mv_musicoteca_royalty_detail")
public class MusicotecaRoyaltyDetail {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "dsp")
    private String dsp;

    @Column(name = "territory")
    private String territory;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "asset_title")
    private String assetTitle;

    @Column(name = "date_user")
    private LocalDateTime dateUser;

    @Column(name = "asset_quantity")
    private Long assetQuantity;

    @Column(name = "total_royalty")
    private BigDecimal totalRoyalty;

    @Column(name = "product_label")
    private String productLabel;

}
