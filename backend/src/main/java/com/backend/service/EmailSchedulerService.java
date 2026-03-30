package com.backend.service;

import com.backend.entity.Recipe;
import com.backend.entity.User;
import com.backend.repository.RecipeRepository;
import com.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailSchedulerService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    // Runs every Friday at 10:00 AM
    @Scheduled(cron = "0 0 10 * * FRI")
    public void sendWeeklyNeighborhoodDigest() {
        List<User> allUsers = userRepository.findAll();

        // Group users by their zone to avoid redundant recipe queries
        Map<String, List<User>> usersByZone = allUsers.stream()
                .collect(Collectors.groupingBy(User::getDefaultZone));

        usersByZone.forEach((zone, users) -> {
            // Get top 3 recipes for this specific zone
            List<Recipe> topRecipes = recipeRepository.findByZoneOrderByNeighborhoodScoreDesc(zone)
                    .stream().limit(3).collect(Collectors.toList());

            if (!topRecipes.isEmpty()) {
                users.forEach(user -> {
                    try {
                        sendHtmlEmail(user.getEmail(), zone, topRecipes);
                    } catch (MessagingException e) {
                        System.err.println("Failed to send email to: " + user.getEmail());
                    }
                });
            }
        });
    }

    private void sendHtmlEmail(String to, String zone, List<Recipe> recipes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("zone", zone);
        context.setVariable("recipes", recipes);

        String htmlContent = templateEngine.process("email-digest", context);

        helper.setTo(to);
        helper.setSubject("NextoBite: Top Trending Recipes in " + zone);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
