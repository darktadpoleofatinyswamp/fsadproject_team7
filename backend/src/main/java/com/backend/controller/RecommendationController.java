package com.backend.controller;

import com.backend.entity.Recipe;
import com.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/suggestions/{recipeId}")
    public ResponseEntity<List<Recipe>> getSuggestions(@PathVariable Long recipeId) {
        return ResponseEntity.ok(recommendationService.getSmartSuggestions(recipeId));
    }

    @GetMapping("/tweak/{recipeId}")
    public ResponseEntity<Map<String, String>> getTweak(
            @PathVariable Long recipeId,
            @RequestParam String diet) {

        String tweak = recommendationService.getSmartTweak(recipeId, diet);
        return ResponseEntity.ok(Map.of(
                "diet", diet,
                "recommendation", tweak
        ));
    }
}
