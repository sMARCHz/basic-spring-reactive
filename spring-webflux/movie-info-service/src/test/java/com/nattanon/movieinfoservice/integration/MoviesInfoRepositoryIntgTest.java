package com.nattanon.movieinfoservice.integration;

import com.nattanon.movieinfoservice.domain.MovieInfo;
import com.nattanon.movieinfoservice.repository.MoviesInfoRepository;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class MoviesInfoRepositoryIntgTest {

    @Autowired
    MoviesInfoRepository moviesInfoRepository;

    @BeforeEach
    void setUp() {
        List<MovieInfo> initialData = Arrays.asList(
                new MovieInfo(null, "Batman Begins", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, Arrays.asList("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises", 2012, Arrays.asList("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );
        moviesInfoRepository.saveAll(initialData)
                .blockLast(); // block others from running task until finish saving
    }

    @AfterEach
    void tearDown() {
        moviesInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        var movieInfo = moviesInfoRepository.findAll().log();
        StepVerifier.create(movieInfo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        var movieInfo = moviesInfoRepository.findById("abc").log();
        StepVerifier.create(movieInfo)
                .assertNext(movieInfo1 -> {
                    assertEquals("Dark Knight Rises", movieInfo1.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        MovieInfo newMovie = new MovieInfo(null, "Batman Begins1", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        var movieInfo = moviesInfoRepository.save(newMovie).log();
        StepVerifier.create(movieInfo)
                .assertNext(movieInfo1 -> {
                    assertNotNull(movieInfo1.getId());
                    assertEquals("Batman Begins1", movieInfo1.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        var movieInfo = moviesInfoRepository.findById("abc").block();
        movieInfo.setYear(2022);

        var movieInfoMono = moviesInfoRepository.save(movieInfo).log();
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo1 -> {
                    assertEquals(2022, movieInfo1.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        moviesInfoRepository.deleteById("abc").block();

        var movieInfo = moviesInfoRepository.findAll().log();
        StepVerifier.create(movieInfo)
                .expectNextCount(2)
                .verifyComplete();
    }
}