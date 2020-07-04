package com.irfanirawansukirman.pipileman.di

import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProvider()
}