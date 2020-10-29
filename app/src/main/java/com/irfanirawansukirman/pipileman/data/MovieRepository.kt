package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Movie
import com.irfanirawansukirman.pipileman.data.model.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    interface Remote {
        suspend fun getPopularMovies(): Flow<UIState<List<Result>>>
    }

    interface RemoteFake {
        suspend fun getFakePopularMovies(): Flow<kotlin.Result<Movie>>
    }

    interface Cache {
        suspend fun insertLocalMovie(movieEnt: MovieEnt)
        suspend fun getLocalMovie(movieId: Long): MovieEnt?
        suspend fun getAllLocalMovies(): List<MovieEnt>?
    }

    interface CacheFake {
        suspend fun getFakeLocalMovie(movieId: Long): Flow<kotlin.Result<MovieEnt>>
    }
}