package com.audiometria.audiometria.api.repository.entities;

import org.springframework.data.jpa.domain.Specification;

public class ReportSpecification {

    public static Specification<Reporte> hasName(String reporteNombre) {
        return (root, query, criteriaBuilder) ->
                reporteNombre == null
                        ? criteriaBuilder.conjunction() // Si es null, no aplica el filtro
                        : criteriaBuilder.equal(root.get("reporteNombre"), reporteNombre); // Si no es null, aplica el filtro
    }

}
