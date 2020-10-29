package com.irfanirawansukirman.pipileman

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irfanirawansukirman.pipileman.abstraction.util.MockUtil
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.TestCoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import com.irfanirawansukirman.pipileman.mvvm.movie.MovieVM
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieVMTest {

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var testCoroutineContextProvider = TestCoroutineContextProvider()

    private var viewModel: MovieVM = mock()
    private var movieRepositoryImpl: MovieRepositoryImpl = mock()
    private var movieService: MovieService = mock()
    private var movieDao: MovieDao = mock()

    @ExperimentalCoroutinesApi
    @Before
    fun `setup depends`() {
        movieRepositoryImpl =
            MovieRepositoryImpl(testCoroutineContextProvider, movieService, movieDao)
        viewModel = MovieVM(testCoroutineContextProvider, movieRepositoryImpl)
    }

    @Test
    fun `get movies and not null and empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedMovies = MockUtil.mockMovieLists()

        // when
        viewModel.getFakePopularMovie()

        // then
        viewModel.movie.observeOnce {
            expectedMovies `should be equal to` it.data
        }
    }

    @Test
    fun `get movies but server is error`() = coroutinesRule.runBlockingTest {
        // given
        val expectedErrorServer = MockUtil.getServerError()
        val expectedErrorMovies = MockUtil.getEmptyList()

        // when
        viewModel.getFakePopularMovie(true)

        // then
        viewModel.movie.observeOnce {
            expectedErrorServer `should be equal to` it.error
            expectedErrorMovies `should be equal to` it.data
        }
    }

    @Test
    fun `get movie and not empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedMovie = MockUtil.mockMovieEnt()

        // when
        viewModel.getFakeLocalMovie(0)

        // then
        viewModel.movieObj.observeOnce {
            expectedMovie `should be equal to` it.data
        }
    }

    @Test
    fun `get movie but database is empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedErrorMovie = MockUtil.getEmptyObj<MovieEnt>()
        val expectedErrorDatabase = MockUtil.getCacheError()

        // when
        viewModel.getFakeLocalMovie(0, true)

        // then
        viewModel.movieObj.observeOnce {
            expectedErrorDatabase `should be equal to` it.error
            expectedErrorMovie `should be equal to` it.data
        }
    }
}