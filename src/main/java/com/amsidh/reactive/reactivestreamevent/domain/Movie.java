package com.amsidh.reactive.reactivestreamevent.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Movie implements Serializable {
    private Long id;
    @NonNull
    private String title;
}
