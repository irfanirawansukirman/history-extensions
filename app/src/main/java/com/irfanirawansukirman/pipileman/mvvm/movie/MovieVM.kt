package com.irfanirawansukirman.pipileman.mvvm.movie

import android.app.Application
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.irfanirawansukirman.pipileman.abstraction.base.BaseViewModel
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import dagger.hilt.android.qualifiers.ApplicationContext

interface MovieContract {
    fun getPopularMovies()

    fun insertLocalMovie(movie: MovieEnt)
    fun getLocalMovie(movieId: Long)
    fun getAllLocalMovies()
}

class MovieVM @ViewModelInject constructor(
    @ApplicationContext context: Context,
    coroutineContextProvider: CoroutineContextProvider,
    private val movieRepositoryImpl: MovieRepositoryImpl
) : BaseViewModel(context as Application, coroutineContextProvider), MovieContract {
    
    private val _movie = MutableLiveData<UIState<List<Result>>>()
    val movie: LiveData<UIState<List<Result>>>
        get() = _movie

    private val _movieObj = MutableLiveData<UIState<MovieEnt?>>()
    val movieObj: LiveData<UIState<MovieEnt?>>
        get() = _movieObj

    private val _movieArray = MutableLiveData<UIState<List<MovieEnt>?>>()
    val movieArray: LiveData<UIState<List<MovieEnt>?>>
        get() = _movieArray

    override fun getPopularMovies() {
        _movie.value = UIState.loading()
        executeJob { _movie.value = UIState.success(movieRepositoryImpl.getPopularMovies()) }
        _movie.value = UIState.finish()
    }

    override fun insertLocalMovie(movie: MovieEnt) =
        executeJob { movieRepositoryImpl.insertLocalMovie(movie) }

    override fun getLocalMovie(movieId: Long) {
        _movieObj.value = UIState.loading()
        executeJob { _movieObj.value = UIState.success(movieRepositoryImpl.getLocalMovie(movieId)) }
        _movieObj.value = UIState.finish()
    }

    override fun getAllLocalMovies() {
        _movieArray.value = UIState.loading()
        executeJob { _movieArray.value = UIState.success(movieRepositoryImpl.getAllLocalMovies()) }
        _movieArray.value = UIState.finish()
    }
}