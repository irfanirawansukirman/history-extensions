package com.irfanirawansukirman.pipileman

import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result

object MockUtil {

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