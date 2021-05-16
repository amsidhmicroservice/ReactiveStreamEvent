package com.amsidh.reactive.reactivestreamevent.service.impl;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.domain.MovieEvent;
import com.amsidh.reactive.reactivestreamevent.repositories.MovieRepository;
import com.amsidh.reactive.reactivestreamevent.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@Service
@Data
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Mono<Movie> getMovieById(String id) {
        return this.movieRepository.findById(id);
    }

    @Override
    public Flux<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }

    @Override
    public Flux<MovieEvent> streamMovieEvents() {
        return Flux.<MovieEvent>generate(movieEventSynchronousSink -> movieEventSynchronousSink.next(new MovieEvent(RandomStringUtils.random(10, true,false), new Date())))
                .delayElements(Duration.ofSeconds(1));

    }

}
