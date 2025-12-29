package com.audiometria.audiometria.api.service.totales;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummary;
import org.springframework.data.domain.Page;

public interface TotalesRoyaltyService {

     Page<MusicotecaRoyaltySummary> searchTotalesRoyalty(SearchRequest request);
}
