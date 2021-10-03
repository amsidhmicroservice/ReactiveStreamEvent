package com.amsidh.reactive.reactivestreamevent.controller;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.service.MovieService;
import com.amsidh.reactive.reactivestreamevent.service.MovieSlowService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/movies")
@Data
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class MovieController {

    private final MovieService movieService;
    private final MovieSlowService movieSlowService;

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable String id) {
        return this.movieService.getMovieById(id);
    }

    @GetMapping
    public Flux<Movie> getAllMovies() {
        return this.movieSlowService.getMoviesFromSlowService();
    }

    @GetMapping(value = "/webclient/{minRange}/{maxRange}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Movie> getAllMoviesWebClient(@PathVariable(name = "minRange") Integer minRange, @PathVariable(name = "maxRange") Integer maxRange) {
        Long startTime = System.currentTimeMillis();
        Flux<Movie> movieFlux = this.movieSlowService.getMoviesFromSlowServiceWithWebClient(minRange, maxRange);
        Long endTime = System.currentTimeMillis();
        log.info("Time taken to process request with WebClient way is StartTime {} and EndTime {}  and TotalTime {}", startTime, endTime, (endTime - startTime));
        return movieFlux;
    }

    @GetMapping(value = "/resttemplate/{minRange}/{maxRange}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Movie> getAllMoviesRestTemplate(@PathVariable(name = "minRange") Integer minRange, @PathVariable(name = "maxRange") Integer maxRange) {
        Long startTime = System.currentTimeMillis();

        List<Long> ids = IntStream.range(minRange, maxRange).asLongStream().boxed().collect(Collectors.toList());
        List<List<Long>> numberList = ListUtils.partition(new ArrayList<>(ids), 10000);

        Flux<Movie> movieFlux = Flux.fromStream(numberList.stream().parallel().flatMap(longs -> movieSlowService.getMoviesFromSlowServiceWithRestTemplate(longs).stream()));

        Long endTime = System.currentTimeMillis();
        log.info("Time taken to process request with RestTemplate way is StartTime {} and EndTime {}  and TotalTime {}", startTime, endTime, (endTime - startTime));
        return movieFlux;
    }

    /*@PostMapping(value = "/ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Movie> getAllMoviesForIds(@RequestBody List<Long> ids) {
        return this.movieSlowService.getMoviesFromSlowServiceForIds(ids);
    }*/

    @GetMapping(value = "/resttemplate/nonstream/{minRange}/{maxRange}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> getAllMoviesRestTemplateNonStream(@PathVariable(name = "minRange") Integer minRange, @PathVariable(name = "maxRange") Integer maxRange) {
        Long startTime = System.currentTimeMillis();

        List<Long> ids = IntStream.range(minRange, maxRange).asLongStream().boxed().collect(Collectors.toList());


        List<Movie> movies = movieSlowService.getMoviesFromSlowServiceWithRestTemplate(ids);

        Long endTime = System.currentTimeMillis();
        log.info("Time taken to process request with RestTemplate way is StartTime {} and EndTime {}  and TotalTime {}", startTime, endTime, (endTime - startTime));
        return movies;
    }


    @GetMapping(value = "/ids/{ids}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> getAllMoviesForIds(@PathVariable("ids") List<Long> ids) {
        return this.movieSlowService.getMoviesFromSlowServiceWithRestTemplate(ids);
    }
}
