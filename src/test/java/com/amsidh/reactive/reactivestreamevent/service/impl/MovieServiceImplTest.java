package com.amsidh.reactive.reactivestreamevent.service.impl;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.domain.MovieEvent;
import com.amsidh.reactive.reactivestreamevent.repositories.MovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@DataMongoTest
@ExtendWith({SpringExtension.class})
public class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindAll() {
        Movie movie1 = new Movie("Daar");
        movie1.setId("123");
        Movie movie2 = new Movie("Tujhe Meri Kasam");
        movie2.setId("456");

        Mockito.when(movieRepository.findAll()).thenReturn(Flux.fromStream(Stream.of(movie1, movie2)));
        Flux<Movie> allMovies = movieService.getAllMovies();
        StepVerifier.create(allMovies).expectNextCount(2).verifyComplete();
    }

    @Test
    public void testGetMovieById() {
        Movie movie1 = new Movie("Kaho na pyar hai");
        movie1.setId("456");
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Mono.just(movie1));
        Mono<Movie> movieById = movieService.getMovieById("456");
        StepVerifier.create(movieById).expectNextCount(1).verifyComplete();
    }

    @Test
    public void testGetMovieByIdNotFound() {
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());
        Mono<Movie> movieById = movieService.getMovieById("456");
        StepVerifier.create(movieById).expectNextCount(0).verifyComplete();
    }

    @Test
    public void testFindAllEmptyFlux() {
        Mockito.when(movieRepository.findAll()).thenReturn(Flux.empty());
        Flux<Movie> allMovies = movieService.getAllMovies();
        StepVerifier.create(allMovies).expectNextCount(0).verifyComplete();
    }


    @Test
    public void testStreamMovieEvents() {
        Flux<MovieEvent> streamMovieEvents = movieService.streamMovieEvents();
        StepVerifier.create(streamMovieEvents).expectNext();
    }

}
