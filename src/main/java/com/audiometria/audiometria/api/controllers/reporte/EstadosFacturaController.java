package com.audiometria.audiometria.api.controllers.reporte;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.UserLabelEstado;
import com.audiometria.audiometria.api.service.reporte.UserLabelEstadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/musica")
@Tag(name = "Estados", description = "Musica API V1")
public class EstadosFacturaController {

    private final UserLabelEstadoService service;

    public EstadosFacturaController(UserLabelEstadoService service) {
        this.service = service;
    }

    @PostMapping("/searchEstados")
    public Page<UserLabelEstado> search(@RequestBody SearchRequest request) {
        return service.search(request);
    }

    /**
     * 🔹 Listar estados por filtro (label opcional)
     * Ejemplo:
     * /estado/listar?label=Spotify&anio=2025&mes=6
     */
    @GetMapping("/listar")
    public List<UserLabelEstado> listar(
            @RequestParam(required = false) String label,
            @RequestParam Integer anio,
            @RequestParam Integer mes
    ) {
        return service.listar(label, anio, mes);
    }

    /**
     * 🔹 Guardar/actualizar un solo registro
     */
    @PostMapping("/guardar")
    public UserLabelEstado guardar(@RequestBody UserLabelEstado request) {
        return service.guardarOActualizar(
                request.getUserName(),
                request.getLabelName(),
                request.getAnio(),
                request.getMes(),
                request.getEstado()
        );
    }

    /**
     * 🔹 Guardado masivo (el que usa tu React)
     */
    @PostMapping("/actualizar-lote")
    public void guardarLote(@RequestBody List<UserLabelEstado> lista) {

        lista.forEach(item -> {
            System.out.println("Estado recibido: " + item.getEstado());
        });
        service.guardarLote(lista);
    }


}
