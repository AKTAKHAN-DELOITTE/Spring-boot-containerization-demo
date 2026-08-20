package com.example.spring_boot_containerization.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    private String id;

    @NotBlank(message = "Movie name is required")
    private String movieName;

    @NotBlank(message = "Theatre is required")
    private String theatre;

    @NotBlank(message = "Show time is required")
    private String showTime;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    @Min(value = 0, message = "Available seats cannot be negative")
    private int availableSeats;

    @Min(value = 0, message = "Price cannot be negative")
    private double price;
}

