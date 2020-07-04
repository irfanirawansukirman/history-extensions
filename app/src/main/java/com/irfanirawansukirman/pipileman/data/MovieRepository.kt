package com.irfanirawansukirman.pipileman.data

import com.irfanirawansukirman.pipileman.data.model.Result

interface MovieRepository {
    suspend fun getPopularMovies(): List<Result>
}