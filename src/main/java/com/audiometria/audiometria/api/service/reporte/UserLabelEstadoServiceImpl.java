package com.audiometria.audiometria.api.service.reporte;


import com.audiometria.audiometria.api.pagination.*;
import com.audiometria.audiometria.api.repository.entities.UserLabelEstado;
import com.audiometria.audiometria.api.repository.entities.UserLabelEstadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserLabelEstadoServiceImpl implements UserLabelEstadoService {

    private final UserLabelEstadoRepository repository;

    public UserLabelEstadoServiceImpl(UserLabelEstadoRepository repository) {
        this.repository = repository;
    }

    /**
     * 🔹 Obtener todos por año y mes (con filtro opcional por label)
     */
    @Override
    public List<UserLabelEstado> listar(String label, Integer anio, Integer mes) {

        if (label != null && !label.isEmpty()) {
            return repository.findAllByLabelNameAndAnioAndMes(label, anio, mes);
        }

        return repository.findAllByAnioAndMes(anio, mes);
    }

    /**
     * 🔹 Crear o actualizar estado (clave única: user + label + año + mes)
     */
    @Override
    public UserLabelEstado guardarOActualizar(
            String userName,
            String labelName,
            Integer anio,
            Integer mes,
            String estado
    ) {

        return repository
                .findByUserNameAndLabelNameAndAnioAndMes(userName, labelName, anio, mes)
                .map(registro -> {
                    registro.setEstado(estado);
                    registro.setFechaActualizacion(LocalDateTime.now());
                    return repository.save(registro);
                })
                .orElseGet(() -> {
                    UserLabelEstado nuevo = UserLabelEstado.builder()
                            .userName(userName)
                            .labelName(labelName)
                            .anio(anio)
                            .mes(mes)
                            .estado(estado)
                            .fechaActualizacion(LocalDateTime.now())
                            .build();

                    return repository.save(nuevo);
                });
    }

    /**
     * 🔹 Guardado masivo (para tu tabla React)
     */
    @Override
    public void guardarLote(List<UserLabelEstado> lista) {

        lista.forEach(item -> guardarOActualizar(
                item.getUserName(),
                item.getLabelName(),
                item.getAnio(),
                item.getMes(),
                item.getEstado()
        ));
    }

    /**
     * 🔹 Búsqueda paginada con SearchSpecification (como ya usas)
     */
    @Override
    public Page<UserLabelEstado> search(SearchRequest request) {

        log.info("Searching UserLabelEstado with request: {}", request);

        // 🔐 1. Limpiar filtros peligrosos (evitar que manden userName manualmente)
        List<FilterRequest> cleanedFilters = request.getFilters().stream()
                .filter(f -> !"userName".equalsIgnoreCase(f.getKey()))
                .collect(Collectors.toList());

        request.setFilters(cleanedFilters);

        // 🔎 2. Obtener userName desde request o filtros
        String userName = Optional.ofNullable(request.getProductLabel()) // reutilizas este campo
                .filter(s -> !s.isBlank())
                .orElseGet(() -> request.getFilters().stream()
                        .filter(f -> "userName".equalsIgnoreCase(f.getKey()))
                        .map(FilterRequest::getValue)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .findFirst()
                        .orElse(null));

        // 👑 3. Lógica de admin
        if ("admin".equalsIgnoreCase(userName)) {
            log.info("Usuario admin detectado. Se eliminan filtros de userName.");

            request.setFilters(
                    request.getFilters().stream()
                            .filter(f -> !"userName".equalsIgnoreCase(f.getKey()))
                            .collect(Collectors.toList())
            );

        } else if (userName != null && !userName.isBlank()) {

            // 👤 4. Usuario normal → se fuerza el filtro
            request.getFilters().add(new FilterRequest(
                    "userName",
                    Operator.EQUAL,
                    FieldType.STRING,
                    userName,
                    null,
                    null
            ));

        } else {
            log.warn("userName no enviado; se omitirá ese filtro.");
        }

        // 📅 5. (Opcional) Filtro por año
        boolean hasAnio = request.getFilters().stream()
                .anyMatch(f -> "anio".equalsIgnoreCase(f.getKey()));

        if (!hasAnio) {
            log.info("No se envió año, no se aplica filtro por anio");
        }

        // 📅 6. (Opcional) Filtro por mes
        boolean hasMes = request.getFilters().stream()
                .anyMatch(f -> "mes".equalsIgnoreCase(f.getKey()));

        if (!hasMes) {
            log.info("No se envió mes, no se aplica filtro por mes");
        }

        // 🧠 7. Construcción dinámica
        SearchSpecification<UserLabelEstado> specification =
                new SearchSpecification<>(request);

        Pageable pageable =
                SearchSpecification.getPageable(request.getPage(), request.getSize());

        return repository.findAll(specification, pageable);
    }


}
