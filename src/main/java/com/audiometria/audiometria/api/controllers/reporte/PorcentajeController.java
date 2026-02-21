package com.audiometria.audiometria.api.controllers.reporte;

import com.audiometria.audiometria.api.service.reporte.PorcentajeSystemServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Musica", description = "Musica API V1")
public class PorcentajeController {

    private final PorcentajeSystemServiceImpl porcentajeSystemServiceImpl;

    public PorcentajeController(PorcentajeSystemServiceImpl porcentajeSystemServiceImpl){
        this.porcentajeSystemServiceImpl = porcentajeSystemServiceImpl;
    }


    @GetMapping("/porcentaje/{displayName}")
    public ResponseEntity<BigDecimal> obtenerPorcentajeDistribucion(@PathVariable String displayName) {
        BigDecimal porcentaje = porcentajeSystemServiceImpl.obtenerPorcentajeDistribucion(displayName);
        return ResponseEntity.ok(porcentaje);
    }
}
