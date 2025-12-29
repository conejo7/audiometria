package com.audiometria.audiometria.api.repository.dto.totales;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MusicotecaRoyaltySearchRequest {

    private Integer page = 0;
    private Integer size = 10;
    private String productLabel;
    private BigDecimal euroMin;
    private BigDecimal euroMax;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String sortBy = "totalRoyalty";
    private String sortDirection = "DESC";
}
