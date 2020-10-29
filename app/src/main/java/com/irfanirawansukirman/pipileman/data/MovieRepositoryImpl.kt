package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MovieRepositoryImpl @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val movieService: MovieService,
    private val movieDao: MovieDao
) : MovieRepository.Remote,
    MovieRepository.Cache,
    MovieRepository.RemoteFake,
    MovieRepository.CacheFake {

    // Real scope ==================================================================================
    override suspend fun getPopularMovies(): Flow<UIState<List<Result>>> {
        return flow {
            val response = movieService.getPopular().results ?: emptyList()
            emit(UIState.success(response))
        }.onCompletion {
            emit(UIState.finish())
        }.catch {
            emit(UIState.error(it.message ?: "Something Went Wrong"))
        }.onStart {
            emit(UIState.loading())
        }.flowOn(coroutineContextProvider.io)
    }

    override suspend fun insertLocalMovie(movieEnt: MovieEnt) = movieDao.insertObject(movieEnt)

    override suspend fun getLocalMovie(movieId: Long): MovieEnt? = movieDao.getObject(movieId)

    override suspend fun getAllLocalMovies(): List<MovieEnt>? = movieDao.getAllObject()

    // Test scope ==================================================================================
    override suspend fun getFakePopularMovies() =
        flow {
            val movie = movieService.getPopular()
            emit(kotlin.Result.success(movie))
        }.catch {
            emit(kotlin.Result.failure(it))
        }.flowOn(coroutineContextProvider.io)

    override suspend fun getFakeLocalMovie(movieId: Long) =
        flow {
            val movieEnt = movieDao.getObject(movieId)
            emit(kotlin.Result.success(movieEnt))
        }.catch {
            emit(kotlin.Result.failure(it))
        }.flowOn(coroutineContextProvider.io)

}