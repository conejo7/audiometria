package com.audiometria.audiometria.api.repository.entities.musicoteca;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface MusicotecaRepository extends JpaRepository<Musicoteca,Integer>, JpaSpecificationExecutor<Musicoteca> {


    @Query("SELECT SUM(m.reportedRoyalty) FROM Musicoteca m WHERE m.productLabel = :productLabel AND m.dateUser BETWEEN :startDate AND :endDate")
    BigDecimal obtenerTotalRoyalty(
            @Param("productLabel") String productLabel,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
