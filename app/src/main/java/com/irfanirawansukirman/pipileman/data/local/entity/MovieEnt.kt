package com.irfanirawansukirman.pipileman.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.irfanirawansukirman.pipileman.abstraction.util.TB_MOVIE

@Entity(tableName = TB_MOVIE)
data class MovieEnt(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id")
    val movieId: Long? = 0,

    @ColumnInfo(name = "title")
    val title: String? = "",

    @ColumnInfo(name = "backdrop")
    val backdrop: String? = "",

    @ColumnInfo(name = "poster")
    val poster: String? = "",

    @ColumnInfo(name = "overview")
    val overview: String? = "",

    @ColumnInfo(name = "release_date")
    val releaseDate: String? = "",

    @ColumnInfo(name = "popularity")
    val popularity: Double? = 0.0
)