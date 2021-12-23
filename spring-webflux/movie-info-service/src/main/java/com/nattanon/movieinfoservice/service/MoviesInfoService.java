package com.nattanon.movieinfoservice.service;

import com.nattanon.movieinfoservice.domain.MovieInfo;
import com.nattanon.movieinfoservice.repository.MoviesInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {

    @Autowired
    private MoviesInfoRepository moviesInfoRepository;

    public Flux<MovieInfo> getAllMovieInfos() {
        return moviesInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return moviesInfoRepository.findById(id);
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return moviesInfoRepository.save(movieInfo);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo updatedMovieInfo, String id) {
        return moviesInfoRepository.findById(id)
                .flatMap(movieInfo -> {
                    movieInfo.setYear(updatedMovieInfo.getYear());
                    movieInfo.setCast(updatedMovieInfo.getCast());
                    movieInfo.setName(updatedMovieInfo.getName());
                    movieInfo.setReleaseDate(updatedMovieInfo.getReleaseDate());
                    return moviesInfoRepository.save(movieInfo);
                });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return moviesInfoRepository.deleteById(id);
    }
}
