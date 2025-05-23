package com.audiometria.audiometria.api.repository.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MusicExcelDTO {

    private String usuario;
    private String title;
    private String artist;
    private BigDecimal amountEur;
}
