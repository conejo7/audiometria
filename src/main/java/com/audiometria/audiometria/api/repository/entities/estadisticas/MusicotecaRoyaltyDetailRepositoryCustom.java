package com.audiometria.audiometria.api.repository.entities.estadisticas;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.dto.estadisticas.MusicotecaRoyaltyDetailDTO;
import org.springframework.data.domain.Page;

public interface MusicotecaRoyaltyDetailRepositoryCustom {

    Page<MusicotecaRoyaltyDetailDTO> searchGrouped(
            SearchRequest request,
            String groupBy
    );
}
