package com.backend.dto;

import java.util.List;
public record RecipeRequest(
        String title,
        List<String> ingredients,
        String steps,
        Double neighborhoodScore
) {}