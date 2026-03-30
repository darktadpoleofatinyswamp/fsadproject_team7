package com.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ElementCollection
    private List<String> ingredients;

    @Column(columnDefinition = "TEXT")
    private String steps;

    private String zone;

    private Double neighborhoodScore;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
