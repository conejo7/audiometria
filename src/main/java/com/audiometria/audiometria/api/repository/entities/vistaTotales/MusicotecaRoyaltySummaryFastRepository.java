package com.audiometria.audiometria.api.repository.entities.vistaTotales;

import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicotecaRoyaltySummaryFastRepository
        extends JpaRepository<MusicotecaRoyaltySummaryFast, String>, JpaSpecificationExecutor<MusicotecaRoyaltySummaryFast> {

}