package com.irfanirawansukirman.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

private fun preAndroidMInternetCheck(
    connectivityManager: ConnectivityManager
): Boolean {
    val activeNetwork = connectivityManager.activeNetworkInfo
    if (activeNetwork != null) {
        return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
    }
    return false
}

@RequiresApi(Build.VERSION_CODES.M)
private fun postAndroidMInternetCheck(
    connectivityManager: ConnectivityManager
): Boolean {
    val network = connectivityManager.activeNetwork
    val connection =
        connectivityManager.getNetworkCapabilities(network)

    return connection != null && (
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager

    return if (Build.VERSION.SDK_INT >=
        Build.VERSION_CODES.M
    ) {
        postAndroidMInternetCheck(connectivityManager)
    } else {
        preAndroidMInternetCheck(connectivityManager)
    }
}

fun isInternetAvailable(): Boolean {
    return try {
        val timeoutMs = 1500
        val sock = Socket()
        val sockaddr = InetSocketAddress("8.8.8.8", 53)

        sock.connect(sockaddr, timeoutMs)
        sock.close()

        true
    } catch (e: IOException) {
        false
    }
}

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

@ExperimentalSerializationApi
fun createApiService(okHttpClient: OkHttpClient, url: String): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
}

fun getChuck(appContext: Context) = ChuckInterceptor(appContext)

fun getLogBodyResponse() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)