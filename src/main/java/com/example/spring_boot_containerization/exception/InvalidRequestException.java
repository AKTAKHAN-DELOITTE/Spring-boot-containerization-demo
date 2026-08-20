package com.example.spring_boot_containerization.exception;

/**
 * Thrown for invalid business operations, e.g. booking more seats
 * than are available. Maps to HTTP 400.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}

