package com.irfanirawansukirman.pipileman.di

import android.content.Context
import com.irfanirawansukirman.extensions.createApiService
import com.irfanirawansukirman.extensions.createOkHttpClient
import com.irfanirawansukirman.extensions.getChuck
import com.irfanirawansukirman.extensions.getLogBodyResponse
import com.irfanirawansukirman.pipileman.BuildConfig
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        return createOkHttpClient(getChuck(appContext), getLogBodyResponse())
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return createApiService(okHttpClient, BuildConfig.BASE_URL)
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }
}