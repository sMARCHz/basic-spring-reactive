package com.nattanon.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void flatMapFlux() {
        // Arrange
        // Act
        Flux<String> flatMappedNames = fluxAndMonoGeneratorService.flatMapFlux();
        // Assert
        StepVerifier.create(flatMappedNames).expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E").verifyComplete();
    }

    @Test
    void flatMapAsyncFlux() {
        // Arrange
        // Act
        Flux<String> flatMappedAsyncNames = fluxAndMonoGeneratorService.flatMapAsyncFlux();
        // Assert
//        StepVerifier.create(flatMappedAsyncNames).expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E").verifyComplete();
        StepVerifier.create(flatMappedAsyncNames).expectNextCount(12).verifyComplete();
    }

    @Test
    void flatMapAsyncFlux2() {
        // Arrange
        // Act
        Flux<String> flatMappedAsyncNames = fluxAndMonoGeneratorService.flatMapAsyncFlux2();
        // Assert
//        StepVerifier.create(flatMappedAsyncNames).expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E").verifyComplete();
        StepVerifier.create(flatMappedAsyncNames).expectNextCount(9).verifyComplete();
    }

    @Test
    void flatMapMono() {
        // Arrange
        // Act
        Mono<List<String>> flatMappedAsyncName = fluxAndMonoGeneratorService.flatMapMono();
        // Assert
        StepVerifier.create(flatMappedAsyncName).expectNext(Arrays.asList("A", "L", "E", "X")).verifyComplete();
    }

    @Test
    void concatMapAsyncFlux() {
        // Arrange
        // Act
        Flux<String> flatMappedAsyncNames = fluxAndMonoGeneratorService.concatMapAsyncFlux();
        // Assert
        StepVerifier.create(flatMappedAsyncNames).expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E").verifyComplete();
    }

    @Test
    void flatMapManyMonoToFlux() {
        // Arrange
        // Act
        Flux<String> flatMappedAsyncName = fluxAndMonoGeneratorService.flatMapManyMonoToFlux();
        // Assert
        StepVerifier.create(flatMappedAsyncName).expectNext("A", "L", "E", "X").verifyComplete();
    }

    @Test
    void transformFlux() {
        // Arrange
        // Act
        Flux<String> transformedNames = fluxAndMonoGeneratorService.transformFlux();
        // Assert
        StepVerifier.create(transformedNames).expectNext("ALEX", "BEN", "CHLOE").verifyComplete();
    }

    @Test
    void defaultIfEmptyFlux() {
        // Arrange
        // Act
        Flux<String> defaultedNames = fluxAndMonoGeneratorService.defaultIfEmptyFlux();
        // Assert
        StepVerifier.create(defaultedNames).expectNext("default").verifyComplete();
    }

    @Test
    void switchIfEmpty() {
        // Arrange
        // Act
        Flux<String> defaultedNames = fluxAndMonoGeneratorService.switchIfEmpty();
        // Assert
        StepVerifier.create(defaultedNames).expectNext("D", "E", "F", "A", "U", "L", "T").verifyComplete();
    }

    @Test
    void concatFlux() {
        // Arrange
        // Act
        Flux<String> concatAlphabets = fluxAndMonoGeneratorService.concatFlux();
        // Assert
        StepVerifier.create(concatAlphabets).expectNext("a", "b", "c", "d", "e", "f").verifyComplete();
    }

    @Test
    void concatFluxWithFlux() {
        // Arrange
        // Act
        Flux<String> concatedAlphabets = fluxAndMonoGeneratorService.concatFluxWithFlux();
        // Assert
        StepVerifier.create(concatedAlphabets).expectNext("a", "b", "c", "d", "e", "f").verifyComplete();
    }

    @Test
    void concatMonoWithMono() {
        // Arrange
        // Act
        Flux<String> concatedAlphabets = fluxAndMonoGeneratorService.concatMonoWithMono();
        // Assert
        StepVerifier.create(concatedAlphabets).expectNext("a", "b").verifyComplete();
    }

    @Test
    void mergeFlux() {
        // Arrange
        // Act
        Flux<String> mergedAlphabets = fluxAndMonoGeneratorService.mergeFlux();
        // Assert
        StepVerifier.create(mergedAlphabets).expectNext("a", "d", "b", "e", "c", "f").verifyComplete();
        StepVerifier.create(mergedAlphabets).expectNextCount(6).verifyComplete();
    }

    @Test
    void mergeFluxWithFlux() {
        // Arrange
        // Act
        Flux<String> mergedAlphabets = fluxAndMonoGeneratorService.mergeFluxWithFlux();
        // Assert
        StepVerifier.create(mergedAlphabets).expectNext("a", "d", "b", "e", "c", "f").verifyComplete();
        StepVerifier.create(mergedAlphabets).expectNextCount(6).verifyComplete();
    }

    @Test
    void mergeMonoWithMono() {
        // Arrange
        // Act
        Flux<String> mergedAlphabets = fluxAndMonoGeneratorService.mergeMonoWithMono();
        // Assert
        StepVerifier.create(mergedAlphabets).expectNext("a", "b").verifyComplete();
        StepVerifier.create(mergedAlphabets).expectNextCount(2).verifyComplete();
    }

    @Test
    void mergeSequentialFlux() {
        // Arrange
        // Act
        Flux<String> mergedAlphabets = fluxAndMonoGeneratorService.mergeSequentialFlux();
        // Assert
        StepVerifier.create(mergedAlphabets).expectNext("a", "b", "c", "d", "e", "f").verifyComplete();
    }

    @Test
    void zipFlux() {
        // Arrange
        // Act
        Flux<String> zippedAlphabets = fluxAndMonoGeneratorService.zipFlux();
        // Assert
        StepVerifier.create(zippedAlphabets).expectNext("ad", "be", "cf").verifyComplete();
    }

    @Test
    void zipFlux2() {
        // Arrange
        // Act
        Flux<String> zippedString = fluxAndMonoGeneratorService.zipFlux2();
        // Assert
        StepVerifier.create(zippedString).expectNext("1ad4", "2be5", "3cf6").verifyComplete();
    }

    @Test
    void zipFluxWithFlux() {
        // Arrange
        // Act
        Flux<String> zippedAlphabets = fluxAndMonoGeneratorService.zipFluxWithFlux();
        // Assert
        StepVerifier.create(zippedAlphabets).expectNext("ad", "be", "cf").verifyComplete();
    }

    @Test
    void zipMonoWithMono() {
        // Arrange
        // Act
        Mono<String> zippedAlphabets = fluxAndMonoGeneratorService.zipMonoWithMono();
        // Assert
        StepVerifier.create(zippedAlphabets).expectNext("ba").verifyComplete();
    }
}