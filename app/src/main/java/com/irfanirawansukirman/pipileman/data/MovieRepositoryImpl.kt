package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Result> =
        movieService.getPopular().results!!

    override suspend fun insertLocalMovie(movieEnt: MovieEnt) = movieDao.insertObject(movieEnt)

    override suspend fun getLocalMovie(movieId: Long): MovieEnt? = movieDao.getObject(movieId)

    override suspend fun getAllLocalMovies(): List<MovieEnt>? = movieDao.getAllObject()
}