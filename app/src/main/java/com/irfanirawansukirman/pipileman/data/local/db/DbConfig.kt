package com.irfanirawansukirman.pipileman.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt

@Database(entities = [MovieEnt::class], version = 1, exportSchema = false)
abstract class DbConfig : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}