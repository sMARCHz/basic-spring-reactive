package com.nattanon.movieinfoservice.service;

import com.nattanon.movieinfoservice.domain.MovieInfo;
import com.nattanon.movieinfoservice.repository.MoviesInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {

    @Autowired
    private MoviesInfoRepository moviesInfoRepository;

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return moviesInfoRepository.save(movieInfo);
    }
}
