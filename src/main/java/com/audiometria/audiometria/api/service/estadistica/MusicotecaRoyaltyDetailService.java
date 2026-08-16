package com.audiometria.audiometria.api.service.estadistica;

import com.audiometria.audiometria.api.pagination.*;
import com.audiometria.audiometria.api.repository.dto.estadisticas.MusicotecaRoyaltyDetailDTO;
import com.audiometria.audiometria.api.repository.entities.estadisticas.MusicotecaRoyaltyDetail;
import com.audiometria.audiometria.api.repository.entities.estadisticas.MusicotecaRoyaltyDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicotecaRoyaltyDetailService {

    private final MusicotecaRoyaltyDetailRepository repository;


    public Page<MusicotecaRoyaltyDetailDTO> search(SearchRequest request) {

        log.info("Searching royalty detail {}", request);

        // Evita que sobrescriban el usuario
        request.setFilters(
                request.getFilters().stream()
                        .filter(f -> !"userName".equalsIgnoreCase(f.getKey()))
                        .collect(Collectors.toList())
        );

        String userName = Optional.ofNullable(request.getProductLabel())
                .filter(s -> !s.isBlank())
                .orElseGet(() -> request.getFilters().stream()
                        .filter(f -> "userName".equalsIgnoreCase(f.getKey()))
                        .map(FilterRequest::getValue)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .findFirst()
                        .orElse(null));

        if (!"admin".equalsIgnoreCase(userName)
                && userName != null
                && !userName.isBlank()) {

            request.getFilters().add(
                    new FilterRequest(
                            "userName",
                            com.audiometria.audiometria.api.pagination.Operator.EQUAL,
                            com.audiometria.audiometria.api.pagination.FieldType.STRING,
                            userName,
                            null,
                            null
                    )
            );
        }

        SearchSpecification<MusicotecaRoyaltyDetail> specification =
                new SearchSpecification<>(request);

        Pageable pageable =
                SearchSpecification.getPageable(
                        request.getPage(),
                        request.getSize());

        return repository.findAll(specification, pageable)
                .map(this::toDto);
    }

    /**
     * Convierte la entidad al DTO.
     */
    private MusicotecaRoyaltyDetailDTO toDto(MusicotecaRoyaltyDetail entity) {

        MusicotecaRoyaltyDetailDTO dto = new MusicotecaRoyaltyDetailDTO();

        dto.setId(entity.getId());
        dto.setUserName(entity.getUserName());
        dto.setDsp(entity.getDsp());
        dto.setTerritory(entity.getTerritory());
        dto.setProductTitle(entity.getProductTitle());
        dto.setAssetTitle(entity.getAssetTitle());
        dto.setAssetQuantity(entity.getAssetQuantity());
        dto.setTotalRoyalty(entity.getTotalRoyalty());
        dto.setDateUser(entity.getDateUser());

        return dto;
    }

    public Page<MusicotecaRoyaltyDetailDTO> searchGrouped(
            MusicotecaRoyaltyGroupRequest groupRequest) {

        log.info("Searching grouped royalty detail {}", groupRequest);

        SearchRequest request = groupRequest.getSearchRequest();
        String groupBy = groupRequest.getGroupBy();

        // Validar groupBy
        if (groupBy == null || groupBy.isBlank()) {
            throw new IllegalArgumentException(
                    "El campo groupBy es obligatorio"
            );
        }

        if (!groupBy.equals("dsp")
                && !groupBy.equals("territory")
                && !groupBy.equals("productTitle")
                && !groupBy.equals("assetTitle")) {

            throw new IllegalArgumentException(
                    "groupBy no permitido: " + groupBy
            );
        }

        /*
         * Evita que sobrescriban el usuario
         */
        request.setFilters(
                request.getFilters().stream()
                        .filter(f -> !"userName".equalsIgnoreCase(f.getKey()))
                        .collect(Collectors.toList())
        );

        /*
         * Obtiene el usuario.
         *
         * Mantengo exactamente la lógica que ya tienes
         * en tu método search().
         */
        String userName = Optional.ofNullable(request.getProductLabel())
                .filter(s -> !s.isBlank())
                .orElseGet(() -> request.getFilters().stream()
                        .filter(f -> "userName".equalsIgnoreCase(f.getKey()))
                        .map(FilterRequest::getValue)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .findFirst()
                        .orElse(null));

        /*
         * Si no es admin, agregamos el filtro de usuario.
         */
        if (!"admin".equalsIgnoreCase(userName)
                && userName != null
                && !userName.isBlank()) {

            request.getFilters().add(
                    new FilterRequest(
                            "userName",
                            Operator.EQUAL,
                            FieldType.STRING,
                            userName,
                            null,
                            null
                    )
            );
        }

        /*
         * Finalmente hacemos la consulta agrupada.
         */
        return repository.searchGrouped(request, groupBy);
    }

}
