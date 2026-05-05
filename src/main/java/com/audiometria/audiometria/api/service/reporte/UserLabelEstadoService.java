package com.audiometria.audiometria.api.service.reporte;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.UserLabelEstado;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserLabelEstadoService {

    List<UserLabelEstado> listar(String label, Integer anio, Integer mes);

    UserLabelEstado guardarOActualizar(
            String userName,
            String labelName,
            Integer anio,
            Integer mes,
            String estado
    );

    void guardarLote(List<UserLabelEstado> lista);

    Page<UserLabelEstado> search(SearchRequest request);
}
