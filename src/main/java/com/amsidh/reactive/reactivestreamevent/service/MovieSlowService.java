package com.amsidh.reactive.reactivestreamevent.service;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MovieSlowService {

    Flux<Movie> getMoviesFromSlowService();

    Flux<Movie> getMoviesFromSlowServiceWithWebClient(Integer minRange, Integer maxRange);
    List<Movie> getMoviesFromSlowServiceWithRestTemplate(List<Long> ids);




}
