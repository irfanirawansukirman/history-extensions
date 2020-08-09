package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result

interface MovieRepository {
    interface Remote {
        suspend fun getPopularMovies(): List<Result>
    }

    interface Cache {
        suspend fun insertLocalMovie(movieEnt: MovieEnt)
        suspend fun getLocalMovie(movieId: Long): MovieEnt?
        suspend fun getAllLocalMovies(): List<MovieEnt>?
    }
}