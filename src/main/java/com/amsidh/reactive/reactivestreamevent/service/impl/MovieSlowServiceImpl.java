package com.amsidh.reactive.reactivestreamevent.service.impl;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.service.MovieSlowService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class MovieSlowServiceImpl implements MovieSlowService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    @Override
    public Flux<Movie> getMoviesFromSlowService() {
        return webClient.get().uri("http://localhost:8181/movies").accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Movie.class));
    }

    @Override
    public Flux<Movie> getMoviesFromSlowServiceWithWebClient(Integer minRange, Integer maxRange) {
        List<Long> ids = IntStream.range(minRange, maxRange).asLongStream().boxed().collect(Collectors.toList());

        List<List<Long>> numberList = ListUtils.partition(new ArrayList<>(ids), 10000);
        return Flux.fromIterable(numberList).flatMap(this::getMovieFlux);
    }

    private Flux<Movie> getMovieFlux(List<Long> ids) {
        //log.info("!!!!!!!!!!!!!!!!!!Response received from MovieSlowService for range minRange {} and maxRange {} !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", longs.get(0), longs.get(longs.size()-1));
        return webClient.post().uri("http://localhost:8181/movies/ids").contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ids))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Movie.class))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10))
                        .filter(throwable -> {
                            log.error("Retrying the slow api due to exception :" + throwable.getLocalizedMessage());
                            log.error("Tried for range {} {}", ids.get(0), ids.get(ids.size() - 1));
                            return is5xxServerError(throwable);
                        }))
                .doOnComplete(() -> log.info("Response Received from Slow Service for record from {} to {}", ids.get(0), ids.get(ids.size() - 1)));
    }


    private boolean is5xxServerError(Throwable throwable) {
        //return throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();

        return throwable instanceof WebClientException;
    }


    @Override
    public List<Movie> getMoviesFromSlowServiceWithRestTemplate(List<Long> ids) {
        HttpEntity<List<Long>> httpEntity = new HttpEntity<>(ids);

        ResponseEntity<List<Movie>> response = restTemplate.exchange("http://localhost:8181/movies/ids", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
        });
        return response.getBody();
    }
}
