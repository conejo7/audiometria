package com.audiometria.audiometria.api.service.administracion;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministracionService {

    private final JdbcTemplate jdbcTemplate;

    @Async
    @Transactional
    public void refreshMaterializedView() {
        jdbcTemplate.execute(
                "REFRESH MATERIALIZED VIEW mv_musicoteca_royalty_summary"
        );
    }
}
