package com.nattanon.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void createFlux() {
        // Arrange
        // Act
        Flux<String> names =  fluxAndMonoGeneratorService.createFlux();
        // Assert
        StepVerifier.create(names).expectNext("alex", "ben", "chloe").verifyComplete();
    }

    @Test
    void createFluxByList() {
        // Arrange
        // Act
        Flux<String> names =  fluxAndMonoGeneratorService.createFluxByList();
        // Assert
        StepVerifier.create(names).expectNext("alex", "ben", "chloe").verifyComplete();
    }

    @Test
    void createMono() {
        // Arrange
        // Act
        Mono<String> name =  fluxAndMonoGeneratorService.createMono();
        // Assert
        StepVerifier.create(name).expectNext("alex").verifyComplete();
    }

    @Test
    void mapFlux() {
        // Arrange
        // Act
        Flux<String> uppercasedNames =  fluxAndMonoGeneratorService.mapFlux();
        // Assert
        StepVerifier.create(uppercasedNames).expectNext("ALEX", "BEN", "CHLOE").verifyComplete();
    }

    @Test
    void mapMono() {
        // Arrange
        // Act
        Mono<String> uppercasedName =  fluxAndMonoGeneratorService.mapMono();
        // Assert
        StepVerifier.create(uppercasedName).expectNext("ALEX").verifyComplete();
    }

    @Test
    void filterFlux() {
        // Arrange
        // Act
        Flux<String> filteredNames =  fluxAndMonoGeneratorService.filterFlux();
        // Assert
        StepVerifier.create(filteredNames).expectNext("alex", "chloe").verifyComplete();
    }

    @Test
    void filterFluxWithMap() {
        // Arrange
        // Act
        Flux<String> filteredNames =  fluxAndMonoGeneratorService.filterFluxWithMap();
        // Assert
        StepVerifier.create(filteredNames).expectNext("4-ALEX", "5-CHLOE").verifyComplete();
    }
}