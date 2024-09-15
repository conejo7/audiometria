package com.audiometria.audiometria.api.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte,Integer>, JpaSpecificationExecutor<Reporte> {

}
