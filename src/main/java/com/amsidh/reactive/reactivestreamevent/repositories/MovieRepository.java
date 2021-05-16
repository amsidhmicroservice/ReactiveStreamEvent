package com.amsidh.reactive.reactivestreamevent.repositories;

import com.amsidh.reactive.reactivestreamevent.domain.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
}
