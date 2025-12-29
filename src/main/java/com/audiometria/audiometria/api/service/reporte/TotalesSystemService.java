package com.audiometria.audiometria.api.service.reporte;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TotalesSystemService {

    BigDecimal obtenerTotalRoyalty(String displayName, LocalDate start, LocalDate end);
}
