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

    @Transactional
    public void refreshRoyaltyDetail() {

        System.out.println(">>> INICIANDO REFRESH VIEW");

        try {

            jdbcTemplate.execute(
                    "REFRESH MATERIALIZED VIEW public.mv_musicoteca_royalty_detail"
            );

            System.out.println(">>> REFRESH VIEW OK");

        } catch (Exception e) {

            System.err.println(">>> ERROR REFRESH VIEW: " + e.getMessage());
            e.printStackTrace();

            throw e;
        }
    }


}
