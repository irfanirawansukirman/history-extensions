package com.irfanirawansukirman.pipileman.di

import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideMovieRepositoryImpl(
        movieService: MovieService,
        movieDao: MovieDao
    ): MovieRepositoryImpl = MovieRepositoryImpl(movieService, movieDao)
}