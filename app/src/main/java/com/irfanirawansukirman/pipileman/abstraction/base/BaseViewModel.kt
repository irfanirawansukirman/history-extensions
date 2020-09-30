package com.irfanirawansukirman.pipileman.abstraction.base

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfanirawansukirman.extensions.isInternetAvailable
import com.irfanirawansukirman.extensions.isNetworkAvailable
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import kotlinx.coroutines.*
import java.io.IOException

abstract class BaseViewModel(
    private val applicationContext: Application,
    private val coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel() {

    private val _timeoutException = MutableLiveData<UIState<String>>()
    val timeoutException: LiveData<UIState<String>>
        get() = _timeoutException

    private val _errorException = MutableLiveData<UIState<String>>()
    val errorException: LiveData<UIState<String>>
        get() = _errorException

    private val _ioException = MutableLiveData<UIState<String>>()
    val ioException: LiveData<UIState<String>>
        get() = _ioException

    fun executeJob(
        defaultTimeOut: Long = 10_000,
        execute: suspend () -> Unit
    ) {
        viewModelScope.launch(coroutineContextProvider.io) {
            if (isNetworkAvailable(applicationContext) && isInternetAvailable()) {
                try {
                    withTimeout(defaultTimeOut) {
                        execute()
                    }
                } catch (e: TimeoutCancellationException) {
                    _timeoutException.postValue(UIState.timeout(e.message ?: "Request Timeout"))
                } catch (e: IOException) {
                    _ioException.postValue(UIState.error(e.message ?: "No Internet Connection"))
                } catch (e: Exception) {
                    _errorException.postValue(UIState.error(e.message ?: "Something Went Wrong"))
                }
            } else {
                _errorException.postValue(UIState.error("No Internet Connection"))
            }
        }
    }

    fun executeAsyncJob(
        defaultTimeOut: Long = 10_000,
        execute: suspend () -> Unit
    ) {
        viewModelScope.launch(coroutineContextProvider.io) {
            if (isNetworkAvailable(applicationContext) && isInternetAvailable()) {
                try {
                    withTimeout(defaultTimeOut) {
                        withContext(coroutineContextProvider.io) { execute() }
                    }
                } catch (e: TimeoutCancellationException) {
                    _timeoutException.postValue(UIState.timeout(e.message ?: "Request Timeout"))
                } catch (e: IOException) {
                    _ioException.postValue(UIState.error(e.message ?: "No Internet Connection"))
                } catch (e: Exception) {
                    _errorException.postValue(UIState.error(e.message ?: "Something Went Wrong"))
                }
            } else {
                _errorException.postValue(UIState.error("No Internet Connection"))
            }
        }
    }
}