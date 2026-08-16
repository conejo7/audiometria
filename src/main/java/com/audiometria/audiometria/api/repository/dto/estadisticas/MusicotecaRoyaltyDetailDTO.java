package com.audiometria.audiometria.api.repository.dto.estadisticas;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MusicotecaRoyaltyDetailDTO {

    private String id;

    private String userName;

    private String dsp;

    private String territory;

    private String productTitle;

    private String assetTitle;

    private LocalDateTime dateUser;

    private Long assetQuantity;

    private BigDecimal totalRoyalty;

    private String productLabel;

}
