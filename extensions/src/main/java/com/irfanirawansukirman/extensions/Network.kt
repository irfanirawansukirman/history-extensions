package com.irfanirawansukirman.extensions

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .pingInterval(30, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .apply {
            if (BuildConfig.DEBUG) {
                for (intercept in interceptors) {
                    addInterceptor(intercept)
                }
            }
        }
        .build()
}

inline fun <reified T> createApiService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}