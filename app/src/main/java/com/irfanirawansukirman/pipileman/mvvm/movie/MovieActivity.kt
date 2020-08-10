package com.irfanirawansukirman.pipileman.mvvm.movie

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.irfanirawansukirman.extensions.*
import com.irfanirawansukirman.extensions.widget.getLongFromDate
import com.irfanirawansukirman.extensions.widget.toNewFormat
import com.irfanirawansukirman.pipileman.abstraction.base.BaseActivity
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState
import com.irfanirawansukirman.pipileman.abstraction.ui.UIState.Status.*
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.databinding.MovieActivityBinding
import com.irfanirawansukirman.pipileman.mvvm.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.movie_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class User(val name: String)

@AndroidEntryPoint
class MovieActivity : BaseActivity<MovieActivityBinding>(MovieActivityBinding::inflate),
    MovieContract {

    private val viewModel by viewModels<MovieVM>()

    override fun loadObservers() {
        viewModel.apply { movie.subscribe(this@MovieActivity, ::showMovies) }
    }

    @SuppressLint("SetTextI18n")
    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        getPopularMovies()

        val date = "13/09/2008"
        btnCreate.text = "${date.getLongFromDate().toNewFormat()}\n${date.getLongFromDate()}"
        btnCreate.setOnClickListener {
            showToast("Internet is: ${isNetworkAvailable(this)}")

            createNotification {
                val intentTarget = Intent(this@MovieActivity, MovieActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("a", "Irfan Irawan Sukirman")
                }
                val pendingIntent =
                    PendingIntent.getActivity(this@MovieActivity, 0, intentTarget, 0)
                it.setContentIntent(pendingIntent)
            }

            GlobalScope.launch(Dispatchers.Main) {
                delay(2_000)
                showSnackBar(root, "Data has been updated") {}
            }
        }

        val param = intent?.getStringExtra("a")
        if (param != null) {
            navigation<MainActivity>(requestCode = 1234) {
                putExtra("a", "Irfan Irawan Sukirman")
            }
        }
    }

    override fun continuousCall() {}

    override fun setupViewListener() {
        progress.setOnRefreshListener { getPopularMovies() }
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1234 && resultCode == 1234) {
            showToast("Alhamdulillah")
        }
    }

    private fun showMovies(state: UIState<List<Result>>) {
        when (state.status) {
            LOADING -> showProgress()
            FINISH -> hideProgress()
            SUCCESS -> {
                state.data?.let {
                    Log.d(MovieActivity::class.java.simpleName, it.toString())
                }
            }
            ERROR -> {
                showToast(state.error)
            }
            else -> {
                showToast(state.error)
            }
        }
    }
}