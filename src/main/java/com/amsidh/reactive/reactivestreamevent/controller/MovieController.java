package com.amsidh.reactive.reactivestreamevent.controller;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.domain.MovieEvent;
import com.amsidh.reactive.reactivestreamevent.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movies")
@Data
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable String id) {
        return this.movieService.getMovieById(id);
    }

    @GetMapping
    public Flux<Movie> getAllMovies() {
        return this.movieService.getAllMovies();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieEvent> streamMovieEvents() {
        return this.movieService.streamMovieEvents();
    }
}
