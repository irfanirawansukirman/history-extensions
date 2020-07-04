package com.irfanirawansukirman.pipileman.abstraction.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArray(arr: List<T>)

    @Update
    suspend fun updateObject(obj: T)

    @Delete
    suspend fun deleteObject(obj: T)
}