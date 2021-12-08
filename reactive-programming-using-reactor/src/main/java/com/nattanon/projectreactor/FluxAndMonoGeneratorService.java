package com.nattanon.projectreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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

    // =====================
    // flatMap() Operator
    // =====================
    public Flux<String> flatMapFlux() {
        return Flux.just("alex", "ben", "chloe").flatMap(name -> Flux.fromArray(name.toUpperCase().split(""))).log();
    }
    public Flux<String> flatMapAsyncFlux() {
        Random random = new Random();
        int delay = random.nextInt(1000);
        return Flux.just("alex", "ben", "chloe").flatMap(name -> Flux.fromArray(name.toUpperCase().split("")).delayElements(Duration.ofMillis(delay))).log();
    }
    public Flux<String> flatMapAsyncFlux2() {
        return Flux.just("alex", "ben", "chloe").flatMap(a -> {
            System.out.println("-------------------------");
            System.out.println("Flatmap: " + a);
            return Flux.just("1", "2", "3");
        }).map(a -> {
            System.out.println("Map: " + a);
            return a + "-Mapped";
        }).map(a -> {
            System.out.println("Map2: " + a);
            return a + "2";
        }).log();
    }
    public Mono<List<String>> flatMapMono() {
        Random random = new Random();
        int delay = random.nextInt(1000);
        return Mono.just("alex").flatMap(name -> Mono.just(Arrays.asList(name.toUpperCase().split(""))).delayElement(Duration.ofMillis(delay))).log();
    }

    // =====================
    // concatMap() Operator
    // =====================
    // flatMap -> merge | concatMap -> concat (same order even though async)
    public Flux<String> concatMapAsyncFlux() {
        Random random = new Random();
        int delay = random.nextInt(1000);
        return Flux.just("alex", "ben", "chloe").concatMap(name -> Flux.fromArray(name.toUpperCase().split("")).delayElements(Duration.ofMillis(delay))).log();
    }

    // =====================
    // flatMapMany() Operator
    // =====================
    // Map Mono<List or Array> to Flux
    public Flux<String> flatMapManyMonoToFlux() {
        return Mono.just("alex").flatMapMany(name -> Flux.fromArray(name.toUpperCase().split(""))).log();
    }

    // =====================
    // transform() Operator
    // =====================
    // Use for execute function from function interface
    public Flux<String> transformFlux() {
        Function<Flux<String>, Flux<String>> mapper = name -> name.map(String::toUpperCase);
        return Flux.just("alex", "ben", "chloe").transform(mapper).log();
    }

    // =====================
    // defaultIfEmpty() Operator
    // =====================
    // Return default value that has the same generic of Publisher (i.e. Flux<String> = String (default))
    public Flux<String> defaultIfEmptyFlux() {
        return Flux.just("alex", "ben", "chloe")
                .map(String::toUpperCase)
                .filter(name -> name.length() > 6) // no elements match the condition
                .defaultIfEmpty("default")
                .log();
    }

    // =====================
    // switchIfEmpty() Operator
    // =====================
    public Flux<String> switchIfEmpty() {
        Function<Flux<String>, Flux<String>> mapper = name -> name.flatMap(e -> Flux.just(e.toUpperCase().split("")));

        Flux<String> defaultOperation = Flux.just("default").transform(mapper);
        return Flux.just("alex", "ben", "chloe")
                .map(String::toUpperCase)
                .filter(name -> name.length() > 6) // no elements match the condition
                .switchIfEmpty(defaultOperation)
                .log();
    }

    // =====================
    // concat() Operator
    // =====================
    // concat() subscribe to Publishers in a sequence -> complete first one then subscribe and complete another one
    // static method in Flux (only Flux)
    public Flux<String> concatFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c");
        Flux<String> fluxDEF = Flux.just("d", "e", "f");
        return Flux.concat(fluxABC, fluxDEF).log();
    }

    // =====================
    // concatWith() Operator
    // =====================
    // instance method in Flux and Mono
    public Flux<String> concatFluxWithFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c");
        Flux<String> fluxDEF = Flux.just("d", "e", "f");
        return fluxABC.concatWith(fluxDEF).log();
    }
    public Flux<String> concatMonoWithMono() {
        Mono<String> monoA = Mono.just("a");
        Mono<String> monoB = Mono.just("b");
        return monoA.concatWith(monoB).log();
    }

    // =====================
    // merge() Operator
    // =====================
    // merge() subscribe to both Publishers in the same time -> not sequential
    // static method in Flux (only Flux)
    public Flux<String> mergeFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c").delayElements(Duration.ofMillis(100)); // b,c can't come before a
        Flux<String> fluxDEF = Flux.just("d", "e", "f").delayElements(Duration.ofMillis(125));
        return Flux.merge(fluxABC, fluxDEF).log();
    }

    // =====================
    // mergeWith() Operator
    // =====================
    // instance method in Flux and Mono
    public Flux<String> mergeFluxWithFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c").delayElements(Duration.ofMillis(100));
        Flux<String> fluxDEF = Flux.just("d", "e", "f").delayElements(Duration.ofMillis(125));
        return fluxABC.mergeWith(fluxDEF).log();
    }
    public Flux<String> mergeMonoWithMono() {
        Mono<String> monoA = Mono.just("a").delayElement(Duration.ofMillis(100));
        Mono<String> monoB = Mono.just("b").delayElement(Duration.ofMillis(125));
        return monoA.mergeWith(monoB).log();
    }

    // =====================
    // mergeSequential() Operator
    // =====================
    // static method in Flux
    // mergeSequential() subscribe to both Publishers in the same time but merge in a sequence
    public Flux<String> mergeSequentialFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c").delayElements(Duration.ofMillis(100));
        Flux<String> fluxDEF = Flux.just("d", "e", "f").delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(fluxABC, fluxDEF).log();
    }

    // =====================
    // zip() Operator
    // =====================
    // static method in Flux
    // can zip 2-8 Publishers into 1
    // Publishers are subscribed eagerly
    // group the element at the same order from each Publishers and execute function (i.e. group 1: a and d)
    public Flux<String> zipFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c");
        Flux<String> fluxDEF = Flux.just("d", "e", "f");
        return Flux.zip(fluxABC, fluxDEF, (first, second) -> first + second).log();
    }
    public Flux<String> zipFlux2() {
        Flux<String> fluxABC = Flux.just("a", "b", "c");
        Flux<String> fluxDEF = Flux.just("d", "e", "f");
        Flux<String> flux123 = Flux.just("1", "2", "3");
        Flux<String> flux456 = Flux.just("4", "5", "6");
        return Flux.zip(fluxABC, fluxDEF, flux123, flux456).map(flux -> flux.getT3() + flux.getT1() + flux.getT2() + flux.getT4()).log();
    }

    // =====================
    // zipWith() Operator
    // =====================
    // instance method in Flux and Mono
    public Flux<String> zipFluxWithFlux() {
        Flux<String> fluxABC = Flux.just("a", "b", "c");
        Flux<String> fluxDEF = Flux.just("d", "e", "f");
        return fluxABC.zipWith(fluxDEF, (first, second) -> first + second).log();
    }
    public Mono<String> zipMonoWithMono() {
        Mono<String> monoA = Mono.just("a");
        Mono<String> monoB = Mono.just("b");
        return monoA.zipWith(monoB).map(tuple -> tuple.getT2() + tuple.getT1()).log();
    }
}
