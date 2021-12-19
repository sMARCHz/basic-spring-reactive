package com.nattanon.movieinfoservice.repository;

import com.nattanon.movieinfoservice.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

}
