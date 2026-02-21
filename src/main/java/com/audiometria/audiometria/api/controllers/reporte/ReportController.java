package com.audiometria.audiometria.api.controllers.reporte;


import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.reporte.Reporte;
import com.audiometria.audiometria.api.service.reporte.OperatingSystemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
@RestController
@RequestMapping("/api/v1/report")
@Tag(name = "Report", description = "Report API V1")
public class ReportController {


    private final OperatingSystemService operatingSystemService;

    public ReportController(OperatingSystemService operatingSystemService) {
        this.operatingSystemService = operatingSystemService;
    }


    @GetMapping("/test")
    public String findAllEmployees() {
        return "Hello";
    }



    @PostMapping(value = "/search")
    public Page<Reporte> search(@RequestBody SearchRequest request) {
        return operatingSystemService.searchOperatingSystem(request);
    }
}
