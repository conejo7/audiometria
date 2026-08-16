package com.audiometria.audiometria.api.repository.entities.estadisticas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicotecaRoyaltyDetailRepository extends JpaRepository<MusicotecaRoyaltyDetail, String>,
        JpaSpecificationExecutor<MusicotecaRoyaltyDetail>, MusicotecaRoyaltyDetailRepositoryCustom {
}
