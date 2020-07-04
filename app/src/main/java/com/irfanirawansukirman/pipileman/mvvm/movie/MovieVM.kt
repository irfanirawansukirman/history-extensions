package com.irfanirawansukirman.pipileman.mvvm.movie

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.irfanirawansukirman.pipileman.abstraction.base.BaseViewModel
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.model.Result

interface MovieContract {
    fun getPopularMovies()
}

class MovieVM @ViewModelInject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val movieRepositoryImpl: MovieRepositoryImpl
) : BaseViewModel(coroutineContextProvider), MovieContract {

    private val _movie = MutableLiveData<UIState<List<Result>>>()
    val movie: LiveData<UIState<List<Result>>>
        get() = _movie

    override fun getPopularMovies() {
        _movie.value = UIState.loading()
        executeJob { _movie.value = UIState.success(movieRepositoryImpl.getPopularMovies()) }
    }
}