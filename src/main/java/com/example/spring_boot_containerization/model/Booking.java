package com.example.spring_boot_containerization.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    private String id;

    @NotBlank(message = "Movie id is required")
    private String movieId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Min(value = 1, message = "At least 1 seat must be booked")
    private int seatsBooked;

    private LocalDateTime bookingTime;

    /**
     * e.g. "CONFIRMED", "CANCELLED"
     */
    private String status;
}

