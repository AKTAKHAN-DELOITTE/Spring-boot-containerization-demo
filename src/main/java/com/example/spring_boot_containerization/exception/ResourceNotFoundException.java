package com.example.spring_boot_containerization.exception;

/**
 * Thrown when a requested entity (Movie or Booking) cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

