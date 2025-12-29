package com.audiometria.audiometria.api.repository.entities.totales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicotecaRoyaltySummaryRepository extends JpaRepository<MusicotecaRoyaltySummary, Long>, JpaSpecificationExecutor<MusicotecaRoyaltySummary> {

}
