package com.backend.controller;

import com.backend.dto.RecipeRequest;
import com.backend.entity.Recipe;
import com.backend.entity.User;
import com.backend.repository.UserRepository;
import com.backend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Recipe> createRecipe(
            @RequestPart("recipe") RecipeRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) throws IOException {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // Using the user's defaultZone as the recipe's zone
        Recipe created = recipeService.createRecipe(request, image, username, user.getDefaultZone());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/local-feed")
    public ResponseEntity<List<Recipe>> getLocalFeed(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(recipeService.getRecipesByZone(user.getDefaultZone()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> search(@RequestParam String q) {
        return ResponseEntity.ok(recipeService.searchRecipes(q));
    }
}