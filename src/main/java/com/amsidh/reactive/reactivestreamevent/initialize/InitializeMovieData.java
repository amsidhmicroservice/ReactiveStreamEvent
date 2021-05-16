package com.amsidh.reactive.reactivestreamevent.initialize;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import com.amsidh.reactive.reactivestreamevent.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Data
@AllArgsConstructor
public class InitializeMovieData implements CommandLineRunner {
    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        movieRepository.deleteAll()
                .thenMany(Flux.just("Bazigar", "Hum Apake Hai Kaun", "Diwana", "Koi Mil Gaya", "Daar", "Surfrosh", "Annari", "Monhabate", "Sirf Tum")
                        .map(Movie::new).flatMap(movieRepository::save)
                ).subscribe(null, null, () -> {
            movieRepository.findAll().subscribe(System.out::println);
        });
    }
}
