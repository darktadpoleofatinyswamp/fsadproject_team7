package com.backend.repository;

import com.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByZoneOrderByNeighborhoodScoreDesc(String zone);
    List<Recipe> findByTitleContainingIgnoreCaseOrIngredientsContainingIgnoreCase(String title, String ingredient);
}