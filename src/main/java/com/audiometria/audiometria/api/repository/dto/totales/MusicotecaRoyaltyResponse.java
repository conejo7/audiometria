package com.audiometria.audiometria.api.repository.dto.totales;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MusicotecaRoyaltyResponse {

    private String productLabel;
    private BigDecimal distribucion;
    private BigDecimal euro;
    private LocalDate dateUser;
    private BigDecimal totalRoyalty;
}
