package com.example.blogging_platform_api.controller;

import com.example.blogging_platform_api.dto.StatisticsDto;
import com.example.blogging_platform_api.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<StatisticsDto> getSummaryStatistics() {
        StatisticsDto stats = statisticsService.getGlobalStatistics();
        return ResponseEntity.ok(stats);
    }
}