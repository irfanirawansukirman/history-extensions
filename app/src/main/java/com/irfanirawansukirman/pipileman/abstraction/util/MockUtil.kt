package com.irfanirawansukirman.pipileman.abstraction.util

import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Movie
import com.irfanirawansukirman.pipileman.data.model.Result

object MockUtil {

    fun getMockMovie() =
        Movie(page = 0, results = mockMovieLists(), totalPages = 0, totalResults = 0)

    fun getServerError() = "Server error pak"

    fun getCacheError() = "Data tidak ditemukan"

    fun <T>getEmptyList(): List<T>? = null

    fun <T>getEmptyObj(): T? = null

    fun mockMovieLists() = listOf(
        Result(
            false,
            "",
            listOf(1, 2, 3), 0,
            "",
            "",
            "",
            0.0,
            "",
            "",
            "",
            false,
            0.0,
            0
        )
    )

    fun mockMovieResult() = Result(
        false,
        "",
        listOf(1, 2, 3), 0,
        "",
        "",
        "",
        0.0,
        "",
        "",
        "",
        false,
        0.0,
        0
    )

    fun mockMovieEnt() = MovieEnt(
        0,
        "",
        "",
        "",
        "",
        "",
        0.0
    )
}