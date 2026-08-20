package com.example.spring_boot_containerization.service;

import com.example.spring_boot_containerization.exception.InvalidRequestException;
import com.example.spring_boot_containerization.exception.ResourceNotFoundException;
import com.example.spring_boot_containerization.model.Booking;
import com.example.spring_boot_containerization.model.Movie;
import com.example.spring_boot_containerization.repository.BookingRepository;
import com.example.spring_boot_containerization.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_CANCELLED = "CANCELLED";

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    /**
     * Creates a booking after validating that the referenced movie has enough
     * available seats, then decrements the movie's availableSeats.
     */
    public Booking createBooking(Booking booking) {
        Movie movie = movieRepository.findById(booking.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie not found with id: " + booking.getMovieId()));

        if (booking.getSeatsBooked() <= 0) {
            throw new InvalidRequestException("seatsBooked must be greater than 0");
        }

        if (movie.getAvailableSeats() < booking.getSeatsBooked()) {
            throw new InvalidRequestException(
                    "Not enough available seats for movie: " + movie.getMovieName()
                            + ". Available: " + movie.getAvailableSeats()
                            + ", requested: " + booking.getSeatsBooked());
        }

        movie.setAvailableSeats(movie.getAvailableSeats() - booking.getSeatsBooked());
        movieRepository.save(movie);

        booking.setId(null);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(STATUS_CONFIRMED);
        return bookingRepository.save(booking);
    }

    /**
     * Updates a booking. If the status transitions to CANCELLED, the booked
     * seats are restored to the referenced movie's availableSeats.
     */
    public Booking updateBooking(String id, Booking updatedBooking) {
        Booking existing = getBookingById(id);

        boolean wasConfirmed = STATUS_CONFIRMED.equalsIgnoreCase(existing.getStatus());
        boolean nowCancelled = STATUS_CANCELLED.equalsIgnoreCase(updatedBooking.getStatus());

        if (wasConfirmed && nowCancelled) {
            Movie movie = movieRepository.findById(existing.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Movie not found with id: " + existing.getMovieId()));
            movie.setAvailableSeats(movie.getAvailableSeats() + existing.getSeatsBooked());
            movieRepository.save(movie);
        }

        if (updatedBooking.getStatus() != null) {
            existing.setStatus(updatedBooking.getStatus());
        }
        if (updatedBooking.getCustomerName() != null) {
            existing.setCustomerName(updatedBooking.getCustomerName());
        }

        return bookingRepository.save(existing);
    }

    public void deleteBooking(String id) {
        Booking existing = getBookingById(id);
        bookingRepository.delete(existing);
    }
}

