package com.irfanirawansukirman.pipileman.di

import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepository
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
        coroutineContextProvider: CoroutineContextProvider,
        movieService: MovieService,
        movieDao: MovieDao
    ): MovieRepository.Remote = MovieRepositoryImpl(coroutineContextProvider, movieService, movieDao)
}