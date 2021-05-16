package com.amsidh.reactive.reactivestreamevent.service;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.domain.MovieEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Mono<Movie> getMovieById(String id);

    Flux<Movie> getAllMovies();

    Flux<MovieEvent> streamMovieEvents();
}
