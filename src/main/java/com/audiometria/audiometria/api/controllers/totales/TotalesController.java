package com.audiometria.audiometria.api.controllers.totales;

import com.audiometria.audiometria.api.pagination.MusicotecaRoyaltyGroupRequest;
import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.dto.estadisticas.MusicotecaRoyaltyDetailDTO;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummary;
import com.audiometria.audiometria.api.repository.entities.vistaTotales.MusicotecaRoyaltySummaryFast;
import com.audiometria.audiometria.api.service.estadistica.MusicotecaRoyaltyDetailService;
import com.audiometria.audiometria.api.service.reporte.TotalesSystemService;
import com.audiometria.audiometria.api.service.totales.TotalesRoyaltyService;
import com.audiometria.audiometria.api.service.totalesFast.TotalesRoyaltyFastService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Musica", description = "Musica API V1")
public class TotalesController {

    private final TotalesRoyaltyService totalesRoyaltyService;

    private final TotalesRoyaltyFastService totalesRoyaltyFastService;

    private final MusicotecaRoyaltyDetailService musicotecaRoyaltyDetailService;

    public TotalesController(TotalesRoyaltyService totalesRoyaltyService, TotalesRoyaltyFastService totalesRoyaltyFastService, MusicotecaRoyaltyDetailService musicotecaRoyaltyDetailService) {
        this.totalesRoyaltyService = totalesRoyaltyService;
        this.totalesRoyaltyFastService = totalesRoyaltyFastService;
        this.musicotecaRoyaltyDetailService = musicotecaRoyaltyDetailService;
    }

    @PostMapping(value = "/totalroyalty")
    public Page<MusicotecaRoyaltySummary> search(@RequestBody SearchRequest request) {
        return totalesRoyaltyService.searchTotalesRoyalty(request);
    }

    @PostMapping(value = "/totalroyalty-fast")
    public Page<MusicotecaRoyaltySummaryFast> searchFast(@RequestBody SearchRequest request) {
        return totalesRoyaltyFastService.searchTotalesRoyaltyFast(request);
    }


    @PostMapping("/estadisticas")
    public Page<MusicotecaRoyaltyDetailDTO> searchEstadistica(@RequestBody SearchRequest request) {
        return musicotecaRoyaltyDetailService.search(request);
    }

    @PostMapping("/estadisticasGrouped")
    public Page<MusicotecaRoyaltyDetailDTO> searchEstadisticaGrouped(@RequestBody MusicotecaRoyaltyGroupRequest request) {
        return musicotecaRoyaltyDetailService.searchGrouped(request);
    }
}

