package com.project.fitness.controller;

import com.project.fitness.model.Recommendation;
import com.project.fitness.services.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    private RecommendationService recommendationService
@PostMapping("/generate")
    public ResponseEntity<Recommendation> generateRecommendation(){
     Recommendation recommendation = recommendationService.generateRecommendation();
     return ResponseEntity.ok(recommendation);
}
}
