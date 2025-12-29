package com.audiometria.audiometria.api.service.reporte;

import com.audiometria.audiometria.api.repository.entities.musicoteca.MusicotecaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
public class TotalesSystemServiceImpl implements TotalesSystemService {

    private final MusicotecaRepository musicotecaRepository;


    public TotalesSystemServiceImpl(MusicotecaRepository musicotecaRepository) {
        this.musicotecaRepository = musicotecaRepository;
    }

    public BigDecimal obtenerTotalRoyalty(String displayName, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59, 999999999);

        return musicotecaRepository.obtenerTotalRoyalty(displayName, startDateTime, endDateTime);
    }

}
