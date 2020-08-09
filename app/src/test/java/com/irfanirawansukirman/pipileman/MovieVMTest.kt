package com.irfanirawansukirman.pipileman

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.TestCoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import com.irfanirawansukirman.pipileman.mvvm.movie.MovieVM
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieVMTest {

    private var testCoroutineContextProvider: TestCoroutineContextProvider = mock()
    private var viewModel: MovieVM = mock()
    private var movieRepositoryImpl: MovieRepositoryImpl = mock()

    private var movieService: MovieService = mock()
    private var movieDao: MovieDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun `setup depends`() {
        movieRepositoryImpl = MovieRepositoryImpl(movieService, movieDao)
        testCoroutineContextProvider = TestCoroutineContextProvider()
        viewModel = MovieVM(testCoroutineContextProvider, movieRepositoryImpl)
    }

    @Test
    fun `get all popular movies from API`() {
        coroutinesRule.runBlockingTest {
            whenever(movieRepositoryImpl.getPopularMovies()).thenReturn(MockUtil.mockMovieLists())

            viewModel.getPopularMovies()

            assertEquals(viewModel.movie.value?.data, MockUtil.mockMovieLists())
        }
    }

    @Test
    fun `get a popular movie from cache`() {
        coroutinesRule.runBlockingTest {
            whenever(movieRepositoryImpl.getLocalMovie(0)).thenReturn(MockUtil.mockMovieEnt())

            viewModel.getLocalMovie(0)

            assertEquals(viewModel.movieObj.value?.data, MockUtil.mockMovieEnt())
        }
    }
}