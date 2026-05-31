package com.audiometria.audiometria.api.service.totalesFast;

import com.audiometria.audiometria.api.pagination.*;
import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummary;
import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummaryRepository;
import com.audiometria.audiometria.api.repository.entities.vistaTotales.MusicotecaRoyaltySummaryFast;
import com.audiometria.audiometria.api.repository.entities.vistaTotales.MusicotecaRoyaltySummaryFastRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TotalesRoyaltyFastServiceImpl implements TotalesRoyaltyFastService {

    private final MusicotecaRoyaltySummaryFastRepository musicotecaRoyaltySummaryRepository;


    public TotalesRoyaltyFastServiceImpl(MusicotecaRoyaltySummaryFastRepository musicotecaRoyaltySummaryRepository) {
        this.musicotecaRoyaltySummaryRepository = musicotecaRoyaltySummaryRepository;
    }

    public Page<MusicotecaRoyaltySummaryFast> searchTotalesRoyaltyFast(SearchRequest request) {
        log.info("Searching totales with request: {}", request);
        // Filtra cualquier intento de sobreescribir el user_id
        List<FilterRequest> cleanedFilters = request.getFilters().stream()
                .filter(f -> !"user_id".equalsIgnoreCase(f.getKey()))
                .collect(Collectors.toList());

        request.setFilters(cleanedFilters);
        String productLabel = Optional.ofNullable(request.getProductLabel())
                .filter(s -> !s.isBlank())
                .orElseGet(() -> request.getFilters().stream()
                        .filter(f -> "productLabel".equalsIgnoreCase(f.getKey()))
                        .map(FilterRequest::getValue)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .findFirst()
                        .orElse(null));

        if ("admin".equalsIgnoreCase(productLabel)) {
            log.info("Usuario admin detectado. Eliminando filtro productLabel para mostrar todos los registros.");
            request.setFilters(
                    request.getFilters().stream()
                            .filter(f -> !"productLabel".equalsIgnoreCase(f.getKey()))
                            .collect(Collectors.toList())
            );
        } else if (productLabel != null && !productLabel.isBlank()) {
            // Caso normal: filtrar por el label del usuario
            request.getFilters().add(new FilterRequest(
                    "productLabel",
                    Operator.EQUAL,
                    FieldType.STRING,
                    productLabel,
                    null, null
            ));
        } else {
            log.warn("product_label no enviado; se omitirá ese filtro.");
        }

        SearchSpecification<MusicotecaRoyaltySummaryFast> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return musicotecaRoyaltySummaryRepository.findAll(specification, pageable);
    }
}
