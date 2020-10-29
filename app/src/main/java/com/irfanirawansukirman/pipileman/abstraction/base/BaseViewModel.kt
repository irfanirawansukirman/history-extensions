package com.irfanirawansukirman.pipileman.abstraction.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import kotlinx.coroutines.*

abstract class BaseViewModel(private val coroutineContextProvider: CoroutineContextProvider) :
    ViewModel() {

    private val _timeoutException = MutableLiveData<UIState<String>>()
    val timeoutException: LiveData<UIState<String>>
        get() = _timeoutException

    private val _errorException = MutableLiveData<UIState<String>>()
    val errorException: LiveData<UIState<String>>
        get() = _errorException

    fun executeJob(
        execute: suspend () -> Unit
    ) {
        viewModelScope.launch(coroutineContextProvider.io) {
            try {
                withTimeout(10_000) { execute() }
            } catch (e: TimeoutCancellationException) {
                // timeOutException(Throwable("Request Timeout")) // 408 for error code
                _timeoutException.postValue(UIState.timeout(e.message ?: "Request Timeout"))
            } catch (e: Exception) {
                // errorException(Throwable(e.message ?: "Something Went Wrong")) // 500 for error code
                _errorException.postValue(UIState.error(e.message ?: "Something Went Wrong"))
            }
        }
    }

    fun executeAsyncJob(
        execute: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                withTimeout(10_000) { withContext(Dispatchers.IO) { execute() } }
            } catch (e: TimeoutCancellationException) {
                // timeOutException(Throwable("Request Timeout")) // 408 for error code
                _timeoutException.postValue(UIState.timeout(e.message ?: "Request Timeout"))
            } catch (e: Exception) {
                // errorException(Throwable(e.message ?: "Something Went Wrong")) // 500 for error code
                _errorException.postValue(UIState.error(e.message ?: "Something Went Wrong"))
            }
        }
    }
}