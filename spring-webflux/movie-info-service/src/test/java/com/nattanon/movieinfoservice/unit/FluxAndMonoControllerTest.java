package com.nattanon.movieinfoservice.unit;

import com.nattanon.movieinfoservice.controller.FluxAndMonoController;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
class FluxAndMonoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testFlux() {
        webTestClient.get()
                .uri("/test/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void testFluxApproach2() {
        var flux = webTestClient.get()
                .uri("/test/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(flux).expectNext(1, 2, 3).verifyComplete();
    }

    @Test
    void testFluxApproach3() {
        webTestClient.get()
                .uri("/test/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    var responseBody = listEntityExchangeResult.getResponseBody();
                    assertEquals(3, Objects.requireNonNull(responseBody).size());
                });
    }

    @Test
    void testMono() {
        webTestClient.get()
                .uri("/test/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals("Hello world", responseBody);
                });
    }

    @Test
    void testStream() {
        var stream = webTestClient.get()
                .uri("/test/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();
        StepVerifier.create(stream).expectNext(0L, 1L, 2L, 3L).thenCancel().verify();
    }
}