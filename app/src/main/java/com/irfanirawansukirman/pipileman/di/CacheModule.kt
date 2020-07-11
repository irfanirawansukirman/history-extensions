package com.irfanirawansukirman.pipileman.di

import android.app.Application
import androidx.room.Room
import com.irfanirawansukirman.pipileman.abstraction.util.DB_MOVIE
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.local.db.DbConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideDbConfig(application: Application): DbConfig {
        return Room.databaseBuilder(
            application,
            DbConfig::class.java,
            DB_MOVIE
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(cache: DbConfig): MovieDao {
        return cache.movieDao()
    }
}