package com.nattanon.movieinfoservice.unit.controller;

import com.nattanon.movieinfoservice.controller.MoviesInfoController;
import com.nattanon.movieinfoservice.domain.MovieInfo;
import com.nattanon.movieinfoservice.service.MoviesInfoService;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MoviesInfoService moviesInfoServiceMock;

    static String MOVIE_INFO_URL = "/v1/movieinfos";

    @Test
    void getAllMovieInfos() {
        var movieInfos = Arrays.asList(
                new MovieInfo(null, "Batman Begins", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, Arrays.asList("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises", 2012, Arrays.asList("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );

        when(moviesInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));
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
        var movieId = "abc";
        var movieInfo = new MovieInfo("abc", "Dark Knight Rises", 2012, Arrays.asList("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(moviesInfoServiceMock.getMovieInfoById(movieId)).thenReturn(Mono.just(movieInfo));
        webTestClient.get()
                .uri(MOVIE_INFO_URL + "/{id}", movieId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var returnedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(returnedMovieInfo);
                    assertEquals("Dark Knight Rises", returnedMovieInfo.getName());
                });
    }

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null, "Batman Begins1", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoServiceMock.addMovieInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins1", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );
        webTestClient.post()
                .uri(MOVIE_INFO_URL + "/add")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var returnedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(returnedMovieInfo);
                    assertNotNull(returnedMovieInfo.getId());
                    assertEquals("mockId", returnedMovieInfo.getId());
                });
    }

    @Test
    void addMovieInfo_validation() {
        var movieInfo = new MovieInfo(null, "", -2005, Arrays.asList(""), LocalDate.parse("2005-06-15"));
        webTestClient.post()
                .uri(MOVIE_INFO_URL + "/add")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    var expectedErrorMessage = "movieInfo.cast must be present, movieInfo.name must be present, movieInfo.year must be a positive value";
                    System.out.println("Response Body: " + responseBody);
                    assertNotNull(responseBody);
                    assertEquals(expectedErrorMessage, responseBody);
                });
    }

    @Test
    void updateMovieInfo() {
        String movieId = "abc";
        var movieInfo = new MovieInfo(null, "Dark Knight Rises1", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoServiceMock.updateMovieInfo(isA(MovieInfo.class), isA(String.class))).thenReturn(
                Mono.just(new MovieInfo(movieId, "Dark Knight Rises1", 2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );
        webTestClient.put()
                .uri(MOVIE_INFO_URL + "/update/{id}", movieId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var returnedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(returnedMovieInfo);
                    assertNotNull(returnedMovieInfo.getId());
                    assertEquals("Dark Knight Rises1", returnedMovieInfo.getName());
                });
    }

    @Test
    void deleteMovieInfo() {
        String movieId = "abc";

        when(moviesInfoServiceMock.deleteMovieInfo(isA(String.class))).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri(MOVIE_INFO_URL + "/delete/{id}", movieId)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
    }
}