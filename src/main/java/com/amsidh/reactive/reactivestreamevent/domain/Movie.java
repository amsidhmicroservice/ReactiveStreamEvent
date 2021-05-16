package com.amsidh.reactive.reactivestreamevent.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Movie {
    private String id;
    @NonNull
    private String title;
}
