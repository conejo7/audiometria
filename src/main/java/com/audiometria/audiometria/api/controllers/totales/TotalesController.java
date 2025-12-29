package com.audiometria.audiometria.api.controllers.totales;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.musicoteca.Musicoteca;
import com.audiometria.audiometria.api.repository.entities.totales.MusicotecaRoyaltySummary;
import com.audiometria.audiometria.api.service.reporte.TotalesSystemService;
import com.audiometria.audiometria.api.service.totales.TotalesRoyaltyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Musica", description = "Musica API V1")
public class TotalesController {

    private final TotalesRoyaltyService totalesRoyaltyService;

    public TotalesController(TotalesRoyaltyService totalesRoyaltyService) {
        this.totalesRoyaltyService = totalesRoyaltyService;
    }


    @PostMapping(value = "/totalroyalty")
    public Page<MusicotecaRoyaltySummary> search(@RequestBody SearchRequest request) {
        return totalesRoyaltyService.searchTotalesRoyalty(request);
    }
}
