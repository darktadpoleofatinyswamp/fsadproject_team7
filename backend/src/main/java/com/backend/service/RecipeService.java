package com.backend.service;

import com.backend.dto.RecipeRequest;
import com.backend.entity.Recipe;
import com.backend.entity.User;
import com.backend.repository.RecipeRepository;
import com.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.path}")
    private String uploadPath;

    public Recipe createRecipe(RecipeRequest request, MultipartFile image, String username, String zone) throws IOException {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = saveImage(image);

        Recipe recipe = Recipe.builder()
                .title(request.title())
                .ingredients(request.ingredients())
                .steps(request.steps())
                .neighborhoodScore(request.neighborhoodScore())
                .zone(zone)
                .imageUrl(imageUrl)
                .author(author)
                .build();

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipesByZone(String zone) {
        return recipeRepository.findByZoneOrderByNeighborhoodScoreDesc(zone);
    }

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByTitleContainingIgnoreCaseOrIngredientsContainingIgnoreCase(query, query);
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), root.resolve(filename));

        return "/uploads/" + filename;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
}