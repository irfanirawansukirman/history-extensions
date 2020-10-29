package com.irfanirawansukirman.pipileman.mvvm.movie

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.irfanirawansukirman.pipileman.abstraction.base.BaseViewModel
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.util.MockUtil
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.CoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

interface MovieContract {
    fun getPopularMovies()

    interface Test {
        fun getFakePopularMovie(isError: Boolean = false)
        fun getFakeLocalMovie(movieId: Long, isError: Boolean = false)
    }

    fun insertLocalMovie(movie: MovieEnt)
    fun getLocalMovie(movieId: Long)
    fun getAllLocalMovies()
}

@ExperimentalCoroutinesApi
class MovieVM @ViewModelInject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val movieRepositoryImpl: MovieRepositoryImpl
) : BaseViewModel(coroutineContextProvider), MovieContract, MovieContract.Test {

    // Real scope ==================================================================================
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
        executeJob {
            movieRepositoryImpl
                .getPopularMovies()
                .collect { withContext(coroutineContextProvider.main) { _movie.value = it } }
        }
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

    // Test scope ==================================================================================
    override fun getFakePopularMovie(isError: Boolean) {
        executeJob {
            movieRepositoryImpl
                .getPopularMovies()
                .collect {
                    withContext(coroutineContextProvider.main) {
                        _movie.value = if (!isError) {
                            UIState.success(MockUtil.mockMovieLists())
                        } else {
                            UIState.success(MockUtil.getEmptyList<Result>())
                            UIState.error(MockUtil.getServerError())
                        }
                    }
                }
        }
    }

    override fun getFakeLocalMovie(movieId: Long, isError: Boolean) {
        executeJob {
            _movieObj.value = if (!isError) {
                UIState.success(MockUtil.mockMovieEnt())
            } else {
                UIState.success(MockUtil.getEmptyObj<MovieEnt>())
                UIState.error(MockUtil.getCacheError())
            }
        }
    }
}