package com.irfanirawansukirman.pipileman.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.irfanirawansukirman.pipileman.abstraction.base.BaseDao
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt

@Dao
interface MovieDao : BaseDao<MovieEnt> {

    @Query("SELECT * FROM tb_movie WHERE movie_id = :movieId")
    suspend fun getObject(movieId: Long): MovieEnt

    @Query("SELECT * FROM tb_movie ")
    suspend fun getAllObject(): List<MovieEnt>
}