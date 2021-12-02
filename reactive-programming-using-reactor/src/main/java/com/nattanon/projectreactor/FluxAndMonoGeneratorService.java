package com.nattanon.projectreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.createFlux().subscribe(System.out::println);
    }

    // =====================
    // Create Flux and Mono
    // =====================
    public Flux<String> createFlux() {
        return Flux.just("alex", "ben", "chloe").log();
    }
    public Flux<String> createFluxByList() {
        return Flux.fromIterable(Arrays.asList("alex", "ben", "chloe")).log();
    }
    public Mono<String> createMono() {
        return Mono.just("alex").log();
    }

    // =====================
    // map() Operator
    // =====================
    public Flux<String> mapFlux() {
        return Flux.just("alex", "ben", "chloe").map(String::toUpperCase).log();
    }
    public Mono<String> mapMono() {
        return Mono.just("alex").map(String::toUpperCase).log();
    }

    // =====================
    // filter() Operator
    // =====================
    public Flux<String> filterFlux() {
        return Flux.just("alex", "ben", "chloe").filter(name -> name.length() > 3).log();
    }
    public Flux<String> filterFluxWithMap() {
        return Flux.just("alex", "ben", "chloe").map(String::toUpperCase).filter(name -> name.length() > 3).map(names -> names.length() + "-" + names).log();
    }
}
