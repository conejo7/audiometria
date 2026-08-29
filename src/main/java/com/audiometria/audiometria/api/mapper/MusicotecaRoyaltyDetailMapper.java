package com.audiometria.audiometria.api.mapper;


import com.audiometria.audiometria.api.repository.dto.estadisticas.MusicotecaRoyaltyDetailDTO;
import com.audiometria.audiometria.api.repository.entities.estadisticas.MusicotecaRoyaltyDetail;

import java.util.List;


public interface MusicotecaRoyaltyDetailMapper {

    MusicotecaRoyaltyDetailDTO toDto(MusicotecaRoyaltyDetail entity);

    List<MusicotecaRoyaltyDetailDTO> toDto(List<MusicotecaRoyaltyDetail> entities);

}
