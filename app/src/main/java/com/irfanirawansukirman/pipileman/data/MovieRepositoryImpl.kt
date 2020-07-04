package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieService: MovieService) :
    MovieRepository {
    override suspend fun getPopularMovies(): List<Result> =
        movieService.getPopular().results ?: emptyList()
}