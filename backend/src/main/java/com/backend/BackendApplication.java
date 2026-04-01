package com.backend;

import com.backend.entity.Recipe;
import com.backend.entity.User;
import com.backend.repository.RecipeRepository;
import com.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication(exclude = {
        ContextFunctionCatalogAutoConfiguration.class
})
@EnableScheduling
public class BackendApplication {
    public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
    @Bean
    CommandLineRunner initData(RecipeRepository recipeRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            // Check if we already have an admin user to avoid duplicates on every restart
            if (userRepo.findByUsername("admin").isEmpty()) {

                // 1. Create a Default Admin/Chef User
                User admin = User.builder()
                        .username("admin")
                        .email("admin@nextobite.com")
                        .password(encoder.encode("admin123"))
                        .defaultZone("Hyderabad, India")
                        .build();
                userRepo.save(admin);

                // 2. Create a Sample Recipe
                Recipe r = Recipe.builder()
                        .title("Classic Hyderabadi Chicken Biryani")
                        .ingredients(List.of("Basmati Rice", "Chicken", "Yogurt", "Ghee", "Mint", "Saffron"))
                        .steps("1. Marinate chicken. 2. Parboil rice. 3. Layer and Dum cook for 40 mins.")
                        .zone("Hyderabad, India")
                        .neighborhoodScore(9.9)
                        .author(admin)
                        .build();
                recipeRepo.save(r);

                System.out.println(">>> Seed Data Inserted: Admin user and Sample Biryani created.");
            }
        };
    }
}
