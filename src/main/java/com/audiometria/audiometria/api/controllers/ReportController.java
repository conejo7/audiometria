package com.audiometria.audiometria.api.controllers;


import com.audiometria.audiometria.api.pagination.SearchRequest;
import com.audiometria.audiometria.api.repository.entities.Reporte;
import com.audiometria.audiometria.api.service.OperatingSystemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
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
