package com.irfanirawansukirman.pipileman.mvvm.movie

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.irfanirawansukirman.extensions.widget.gone
import com.irfanirawansukirman.pipileman.abstraction.base.BaseActivity
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState.Status.*
import com.irfanirawansukirman.pipileman.abstraction.util.ext.showToast
import com.irfanirawansukirman.pipileman.abstraction.util.ext.subscribe
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.databinding.MovieActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.movie_activity.*

@AndroidEntryPoint
class MovieActivity : BaseActivity<MovieActivityBinding>(MovieActivityBinding::inflate),
    MovieContract {

    private val viewModel by viewModels<MovieVM>()

    override fun loadObservers() {
        viewModel.apply { movie.subscribe(this@MovieActivity, ::showMovies) }
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        getPopularMovies()

        btnCreate.gone()
    }

    override fun continuousCall() {}

    override fun setupViewListener() {}

    override fun enableBackButton(): Boolean = false

    override fun bindToolbar(): Toolbar? = null

    override fun getPopularMovies() {
        viewModel.getPopularMovies()
    }

    override fun insertLocalMovie(movie: MovieEnt) {
        viewModel.insertLocalMovie(movie)
    }

    override fun getLocalMovie(movieId: Long) {
        viewModel.getLocalMovie(movieId)
    }

    override fun getAllLocalMovies() {
        viewModel.getAllLocalMovies()
    }

    private fun showMovies(state: UIState<List<Result>>) {
        when (state.status) {
            LOADING -> showProgress()
            SUCCESS -> {
                hideProgress()

                state.data?.let {
                    Log.d(MovieActivity::class.java.simpleName, it.toString())
                }
            }
            ERROR -> {
                hideProgress()

                showToast(state.error)
            }
            else -> {
                hideProgress()

                showToast(state.error)
            }
        }
    }
}