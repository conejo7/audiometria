package com.audiometria.audiometria.api.service;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.pagination.SearchSpecification;
import com.audiometria.audiometria.api.repository.entities.Reporte;
import com.audiometria.audiometria.api.repository.entities.ReporteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OperatingSystemService {


    private final ReporteRepository reporteRepository;

    public OperatingSystemService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public Page<Reporte> searchOperatingSystem(SearchRequest request) {
        SearchSpecification<Reporte> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return reporteRepository
                .findAll(specification, pageable);
    }
}
