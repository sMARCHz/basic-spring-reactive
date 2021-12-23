package com.nattanon.movieinfoservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Movie")
public class MovieInfo {
    @Id
    private String id;
    private String name;
    private Integer year;
    private List<String> cast;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
}
