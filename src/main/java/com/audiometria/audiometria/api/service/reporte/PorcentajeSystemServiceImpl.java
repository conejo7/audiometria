package com.audiometria.audiometria.api.service.reporte;

import com.audiometria.audiometria.api.repository.entities.UserLabelMap;
import com.audiometria.audiometria.api.repository.entities.UserLabelMapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class PorcentajeSystemServiceImpl implements PorcentajeSystemService {

    private final UserLabelMapRepository userLabelMapRepository;

    public PorcentajeSystemServiceImpl(UserLabelMapRepository userLabelMapRepository) {
        this.userLabelMapRepository = userLabelMapRepository;
    }

    @Override
    public BigDecimal obtenerPorcentajeDistribucion(String displayName) {
        return userLabelMapRepository.findByLabelName(displayName)
                .map(UserLabelMap::getPorcentaje)
                .orElse(BigDecimal.ZERO); // si no existe, devuelve 0

    }



}
