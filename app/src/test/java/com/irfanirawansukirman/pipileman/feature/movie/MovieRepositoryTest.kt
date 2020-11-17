package com.irfanirawansukirman.pipileman.feature.movie

import androidx.lifecycle.Observer
import com.irfanirawansukirman.pipileman.abstraction.util.MockUtil
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.TestCoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.model.Result
import com.irfanirawansukirman.pipileman.data.remote.MovieService
import com.irfanirawansukirman.pipileman.util.MainCoroutinesRule
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    private var testCoroutineContextProvider = TestCoroutineContextProvider()

    private var movieRepositoryImpl: MovieRepositoryImpl = mock()
    private var movieService: MovieService = mock()
    private var movieDao: MovieDao = mock()

    @Before
    fun `setup depends`() {
        movieRepositoryImpl =
            MovieRepositoryImpl(testCoroutineContextProvider, movieService, movieDao)
    }

    @Test
    fun `get movies and not empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedMovies = MockUtil.getMockMovie()
        movieService.stub { onBlocking { getPopular() } doReturn expectedMovies }

        // when
        val flow = movieRepositoryImpl.getFakePopularMovies()

        // then
        flow.collect {
            it.isSuccess.shouldBeTrue()
            expectedMovies.results `should be equal to` it.getOrNull()?.results
        }
    }

    @Test
    fun `get movies but server is error`() = coroutinesRule.runBlockingTest {
        // given
        val expectedErrorServer = MockUtil.getServerError()
        movieService.stub {
            onBlocking { getPopular() } doAnswer {
                throw IOException(
                    expectedErrorServer
                )
            }
        }

        // when
        val flow = movieRepositoryImpl.getFakePopularMovies()

        // then
        flow.collect {
            it.isFailure.shouldBeTrue()
            expectedErrorServer `should be equal to` it.exceptionOrNull()?.message
        }
    }

    @Test
    fun `get movie and not empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedMovie = MockUtil.mockMovieEnt()
        movieDao.stub { onBlocking { getObject(0) } doReturn expectedMovie }

        // when
        val flow = movieRepositoryImpl.getFakeLocalMovie(0)

        // then
        flow.collect {
            it.isSuccess.shouldBeTrue()
            expectedMovie `should be equal to` it.getOrNull()
        }
    }

    @Test
    fun `get movie but database is empty`() = coroutinesRule.runBlockingTest {
        // given
        val expectedErrorDatabase = MockUtil.getCacheError()
        movieDao.stub {
            onBlocking { getObject(0) } doAnswer {
                throw IOException(
                    expectedErrorDatabase
                )
            }
        }

        // when
        val flow = movieRepositoryImpl.getFakeLocalMovie(0)

        // then
        flow.collect {
            it.isFailure.shouldBeTrue()
            expectedErrorDatabase `should be equal to` it.exceptionOrNull()?.message
        }
    }
}

// Source: https://medium.com/@heyitsmohit/unit-testing-delays-errors-retries-with-kotlin-flows-77ce00d0c2f3
// Source: https://proandroiddev.com/eliminating-coroutine-leaks-in-tests-3af825e7cde2