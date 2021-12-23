package com.nattanon.movieinfoservice.integration;

import com.nattanon.movieinfoservice.domain.MovieInfo;
import com.nattanon.movieinfoservice.repository.MoviesInfoRepository;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerIntgTest {

    @Autowired
    private MoviesInfoRepository moviesInfoRepository;

    @Autowired
    private WebTestClient webTestClient;

    static String MOVIE_INFO_URL = "/v1/movieinfos";

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
    void getAllMovieInfos() {
        webTestClient.get()
                .uri(MOVIE_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getMovieInfoById() {
        String movieId = "abc";
        // approach 1
//        webTestClient.get()
//                .uri(MOVIE_INFO_URL + "/{id}", movieId)
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful()
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(movieInfo);
//                });

        // approach 2
        webTestClient.get()
                .uri(MOVIE_INFO_URL + "/{id}", movieId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name")
                .isEqualTo("Dark Knight Rises");
    }

    @Test
    void addMovieInfo() {
        // given
        var movieInfo = new MovieInfo(null, "Batman Begins", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        // when
        webTestClient.post()
                .uri(MOVIE_INFO_URL + "/add")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(savedMovieInfo);
                    assertNotNull(savedMovieInfo.getId());
                });
        // then
    }

    @Test
    void updateMovieInfo() {
        var movieId = "abc";
        var movieInfo = new MovieInfo(null, "Dark Knight Rises1", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        webTestClient.put()
                .uri(MOVIE_INFO_URL + "/update/{id}", movieId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(updatedMovieInfo);
                    assertNotNull(updatedMovieInfo.getId());
                    assertEquals("Dark Knight Rises1", updatedMovieInfo.getName());
                });
    }

    @Test
    void deleteMovieInfo() {
        String movieId = "abc";
        webTestClient.delete()
                .uri(MOVIE_INFO_URL + "/delete/{id}", movieId)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
        webTestClient.get()
                .uri(MOVIE_INFO_URL)
                .exchange()
                .expectBodyList(MovieInfo.class)
                .hasSize(2);
    }
}