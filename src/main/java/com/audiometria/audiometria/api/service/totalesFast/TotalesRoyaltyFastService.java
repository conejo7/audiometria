package com.audiometria.audiometria.api.service.totalesFast;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummary;
import com.audiometria.audiometria.api.repository.entities.vistaTotales.MusicotecaRoyaltySummaryFast;
import org.springframework.data.domain.Page;

public interface TotalesRoyaltyFastService {

     Page<MusicotecaRoyaltySummaryFast> searchTotalesRoyaltyFast(SearchRequest request);
}
