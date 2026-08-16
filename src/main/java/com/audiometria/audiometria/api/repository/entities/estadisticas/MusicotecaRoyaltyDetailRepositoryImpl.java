package com.audiometria.audiometria.api.repository.entities.estadisticas;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.pagination.SearchSpecification;
import com.audiometria.audiometria.api.repository.dto.estadisticas.MusicotecaRoyaltyDetailDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MusicotecaRoyaltyDetailRepositoryImpl implements MusicotecaRoyaltyDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<MusicotecaRoyaltyDetailDTO> searchGrouped(
            SearchRequest request,
            String groupBy) {

        /*
         * Validamos el campo por el cual se va a agrupar.
         */
        if (!isValidGroupBy(groupBy)) {
            throw new IllegalArgumentException(
                    "Campo groupBy no permitido: " + groupBy
            );
        }

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query =
                cb.createTupleQuery();

        Root<MusicotecaRoyaltyDetail> root =
                query.from(MusicotecaRoyaltyDetail.class);


        /*
         * Campo de agrupación.
         */
        Expression<String> groupExpression =
                getGroupExpression(root, groupBy);


        Expression<BigDecimal> totalRoyalty = cb.sum(root.get("totalRoyalty"));
        /*
         * Fecha.
         */
//        Expression<LocalDateTime> dateExpression =
//                root.get("dateUser");


        /*
         * SUM(totalRoyalty)
         */



        /*
         * SUM(assetQuantity)
         */
        Expression<Long> assetQuantity =
                cb.sum(root.get("assetQuantity"));


        /*
         * SELECT
         */
        query.multiselect(
                groupExpression.alias("groupValue"),
//                dateExpression.alias("dateUser"),
                totalRoyalty.alias("totalRoyalty"),
                assetQuantity.alias("assetQuantity")
        );


        /*
         * Aplicamos los filtros que vienen
         * en SearchRequest.
         */
        SearchSpecification<MusicotecaRoyaltyDetail> specification =
                new SearchSpecification<>(request);

        Predicate predicate =
                specification.toPredicate(
                        root,
                        query,
                        cb
                );

        if (predicate != null) {
            query.where(predicate);
        }


        /*
         * GROUP BY
         *
         * Ejemplo:
         *
         * DSP:
         * GROUP BY dsp, dateUser
         *
         * Territory:
         * GROUP BY territory, dateUser
         */
        query.groupBy(
                groupExpression
        );


        /*
         * ORDER BY
         */
        query.orderBy(
                cb.desc(groupExpression)
        );


        /*
         * Paginación.
         */
        Pageable pageable =
                SearchSpecification.getPageable(
                        request.getPage(),
                        request.getSize()
                );


        /*
         * Ejecutamos la consulta.
         */
        List<Tuple> result =
                entityManager
                        .createQuery(query)
                        .setFirstResult(
                                (int) pageable.getOffset()
                        )
                        .setMaxResults(
                                pageable.getPageSize()
                        )
                        .getResultList();


        /*
         * Convertimos Tuple -> DTO
         */
        List<MusicotecaRoyaltyDetailDTO> content =
                result.stream()
                        .map(tuple ->
                                toDto(tuple, groupBy)
                        )
                        .toList();


        /*
         * Total de grupos para la paginación.
         */
        long total =
                countGrouped(
                        request,
                        groupBy
                );


        return new PageImpl<>(
                content,
                pageable,
                total
        );
    }


    /**
     * Devuelve la columna por la cual
     * se va a realizar el GROUP BY.
     */
    private Expression<String> getGroupExpression(
            Root<MusicotecaRoyaltyDetail> root,
            String groupBy) {

        return switch (groupBy) {

            case "dsp" ->
                    root.get("dsp");

            case "territory" ->
                    root.get("territory");

            case "productTitle" ->
                    root.get("productTitle");

            case "assetTitle" ->
                    root.get("assetTitle");

            default ->
                    throw new IllegalArgumentException(
                            "Campo groupBy no permitido: " + groupBy
                    );
        };
    }


    /**
     * Valida los únicos campos permitidos.
     */
    private boolean isValidGroupBy(String groupBy) {

        return "dsp".equals(groupBy)
                || "territory".equals(groupBy)
                || "productTitle".equals(groupBy)
                || "assetTitle".equals(groupBy);
    }


    /**
     * Convierte el resultado agrupado
     * al DTO que ya tienes.
     */
    private MusicotecaRoyaltyDetailDTO toDto(
            Tuple tuple,
            String groupBy) {

        MusicotecaRoyaltyDetailDTO dto =
                new MusicotecaRoyaltyDetailDTO();


//        dto.setDateUser(
//                tuple.get(
//                        "dateUser",
//                        LocalDateTime.class
//                )
//        );


        dto.setTotalRoyalty(
                tuple.get(
                        "totalRoyalty",
                        BigDecimal.class
                )
        );


        dto.setAssetQuantity(
                tuple.get(
                        "assetQuantity",
                        Long.class
                )
        );


        String value =
                tuple.get(
                        "groupValue",
                        String.class
                );


        /*
         * Colocamos el valor en el campo
         * correspondiente del DTO.
         */
        switch (groupBy) {

            case "dsp":
                dto.setDsp(value);
                break;

            case "territory":
                dto.setTerritory(value);
                break;

            case "productTitle":
                dto.setProductTitle(value);
                break;

            case "assetTitle":
                dto.setAssetTitle(value);
                break;
        }


        return dto;
    }


    /**
     * Cuenta cuántos grupos existen.
     *
     * Esto es necesario porque estamos
     * utilizando Page y paginación.
     */

    private long countGrouped(
            SearchRequest request,
            String groupBy) {

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<String> query =
                cb.createQuery(String.class);

        Root<MusicotecaRoyaltyDetail> root =
                query.from(MusicotecaRoyaltyDetail.class);


        /*
         * Campo por el cual agrupamos.
         */
        Expression<String> groupExpression =
                getGroupExpression(
                        root,
                        groupBy
                );


        /*
         * SELECT del grupo.
         */
        query.select(
                groupExpression
        );


        /*
         * Aplicamos los mismos filtros.
         */
        SearchSpecification<MusicotecaRoyaltyDetail> specification =
                new SearchSpecification<>(request);

        Predicate predicate =
                specification.toPredicate(
                        root,
                        query,
                        cb
                );

        if (predicate != null) {
            query.where(predicate);
        }


        /*
         * GROUP BY
         */
        query.groupBy(
                groupExpression
        );


        /*
         * Cada resultado representa
         * un grupo.
         */
        return entityManager
                .createQuery(query)
                .getResultList()
                .size();
    }


}
