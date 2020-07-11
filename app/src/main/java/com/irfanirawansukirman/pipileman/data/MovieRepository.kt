package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result

interface MovieRepository {
    suspend fun getPopularMovies(): List<Result>

    suspend fun insertLocalMovie(movieEnt: MovieEnt)
    suspend fun getLocalMovie(movieId: Long): MovieEnt?
    suspend fun getAllLocalMovies(): List<MovieEnt>?
}