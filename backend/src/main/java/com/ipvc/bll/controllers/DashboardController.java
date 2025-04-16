package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.DashboardStatsDTO;
import com.ipvc.bll.services.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardStatsDTO getDashboardStats() {
        return dashboardService.getStats();
    }
}
