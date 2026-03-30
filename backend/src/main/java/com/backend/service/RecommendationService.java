package com.backend.service;

import com.backend.entity.Recipe;
import com.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecipeRepository recipeRepository;

    public List<Recipe> getSmartSuggestions(Long recipeId) {
        Recipe target = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .filter(r -> !r.getId().equals(recipeId))
                .filter(r -> calculateJaccardSimilarity(target.getIngredients(), r.getIngredients()) > 0.2) // 20% overlap threshold
                .sorted((r1, r2) -> Double.compare(
                        calculateJaccardSimilarity(target.getIngredients(), r2.getIngredients()),
                        calculateJaccardSimilarity(target.getIngredients(), r1.getIngredients())
                ))
                .limit(5)
                .collect(Collectors.toList());
    }

    private double calculateJaccardSimilarity(List<String> list1, List<String> list2) {
        Set<String> s1 = list1.stream().map(String::toLowerCase).collect(Collectors.toSet());
        Set<String> s2 = list2.stream().map(String::toLowerCase).collect(Collectors.toSet());

        Set<String> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);

        Set<String> union = new HashSet<>(s1);
        union.addAll(s2);

        if (union.isEmpty()) return 0.0;
        return (double) intersection.size() / union.size();
    }

    public String getSmartTweak(Long recipeId, String dietType) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        // Rule-Based Logic (Expert System)
        return switch (dietType.toLowerCase()) {
            case "low-spice" -> "REDUCE: Green chilies and Red chili powder by 50%. ADD: 1 tbsp Yogurt to balance heat.";
            case "vegan" -> "SUBSTITUTE: Replace Ghee/Butter with Coconut Oil or Olive Oil. Use Tofu instead of Paneer.";
            case "keto" -> "REMOVE: Potatoes or Rice. INCREASE: Healthy fats and leafy greens. Use Cauliflower rice as a base.";
            default -> "Enjoy the authentic Hyderabad flavors as is!";
        };
    }
}
