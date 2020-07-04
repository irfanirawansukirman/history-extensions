package com.irfanirawansukirman.pipileman.data.remote

import com.irfanirawansukirman.pipileman.BuildConfig
import com.irfanirawansukirman.pipileman.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String? = BuildConfig.MOVIE_API_KEY
    ): Movie
}