package com.irfanirawansukirman.pipileman.feature.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irfanirawansukirman.pipileman.abstraction.util.MockUtil
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.TestCoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.local.entity.MovieEnt
import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import com.irfanirawansukirman.pipileman.mvvm.movie.MovieVM
import com.irfanirawansukirman.pipileman.util.MainCoroutinesRule
import com.irfanirawansukirman.pipileman.util.getOrAwaitValue
import com.irfanirawansukirman.pipileman.util.observeOnce
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
        // #1
        viewModel.movie.observeOnce {
            expectedMovies `should be equal to` it.data
        }

        // or

        // #2
        expectedMovies `should be equal to` viewModel.movie.getOrAwaitValue()?.data
    }

    @Test
    fun `get movies but server is error`() = coroutinesRule.runBlockingTest {
        // given
        val expectedErrorServer = MockUtil.getServerError()
        val expectedErrorMovies = MockUtil.getEmptyList<Result>()

        // when
        viewModel.getFakePopularMovie(true)

        // then
        // #1
        viewModel.movie.observeOnce {
            expectedErrorServer `should be equal to` it.error
            expectedErrorMovies `should be equal to` it.data
        }

        // or

        // #2
        expectedErrorServer `should be equal to` viewModel.movie.value?.error
        expectedErrorMovies `should be equal to` viewModel.movie.value?.data
    }

    @Test
    fun `get movie and not empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedMovie = MockUtil.mockMovieEnt()

        // when
        viewModel.getFakeLocalMovie(0)

        // then
        // #1
        viewModel.movieObj.observeOnce {
            expectedMovie `should be equal to` it.data
        }

        // or

        // #2
        expectedMovie `should be equal to` viewModel.movieObj.value?.data
    }

    @Test
    fun `get movie but database is empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedErrorDatabase = MockUtil.getCacheError()
        val expectedErrorMovie = MockUtil.getEmptyObj<MovieEnt>()

        // when
        viewModel.getFakeLocalMovie(0, true)

        // then
        // #1
        viewModel.movieObj.observeOnce {
            expectedErrorDatabase `should be equal to` it.error
            expectedErrorMovie `should be equal to` it.data
        }

        // or

        // #2
        expectedErrorDatabase `should be equal to` viewModel.movieObj.value?.error
        expectedErrorMovie `should be equal to` viewModel.movieObj.value?.data
    }
}