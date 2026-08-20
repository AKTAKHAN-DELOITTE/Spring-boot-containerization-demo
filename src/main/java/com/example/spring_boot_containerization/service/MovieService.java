package com.example.spring_boot_containerization.service;

import com.example.spring_boot_containerization.exception.ResourceNotFoundException;
import com.example.spring_boot_containerization.model.Movie;
import com.example.spring_boot_containerization.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    public Movie createMovie(Movie movie) {
        movie.setId(null); // ensure Mongo generates a new id
        return movieRepository.save(movie);
    }

    public List<Movie> createMovies(List<Movie> movies) {
        movies.forEach(movie -> movie.setId(null)); // ensure Mongo generates new ids
        return movieRepository.saveAll(movies);
    }

    public Movie updateMovie(String id, Movie updatedMovie) {
        Movie existing = getMovieById(id);
        existing.setMovieName(updatedMovie.getMovieName());
        existing.setTheatre(updatedMovie.getTheatre());
        existing.setShowTime(updatedMovie.getShowTime());
        existing.setTotalSeats(updatedMovie.getTotalSeats());
        existing.setAvailableSeats(updatedMovie.getAvailableSeats());
        existing.setPrice(updatedMovie.getPrice());
        return movieRepository.save(existing);
    }

    public void deleteMovie(String id) {
        Movie existing = getMovieById(id);
        movieRepository.delete(existing);
    }
}

