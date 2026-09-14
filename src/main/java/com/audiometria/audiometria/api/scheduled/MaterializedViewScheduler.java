package com.audiometria.audiometria.api.scheduled;

import com.audiometria.audiometria.api.service.administracion.AdministracionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MaterializedViewScheduler {

    private final AdministracionService administracionService;


    public MaterializedViewScheduler(AdministracionService musicaService) {
        this.administracionService = musicaService;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "America/Guayaquil")
    public void refreshViews() {
        System.out.println(">>> EJECUTANDO REFRESH MATERIALIZED VIEW");
        administracionService.refreshRoyaltyDetail();

        System.out.println(">>> REFRESH TERMINADO");
    }

}
