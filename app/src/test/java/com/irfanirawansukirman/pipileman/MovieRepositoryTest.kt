package com.irfanirawansukirman.pipileman

import com.irfanirawansukirman.pipileman.abstraction.util.MockUtil
import com.irfanirawansukirman.pipileman.abstraction.util.coroutine.TestCoroutineContextProvider
import com.irfanirawansukirman.pipileman.data.MovieRepositoryImpl
import com.irfanirawansukirman.pipileman.data.local.dao.MovieDao
import com.irfanirawansukirman.pipileman.data.remote.MovieService
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
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    private var testCoroutineContextProvider = TestCoroutineContextProvider()

    private var movieRepositoryImpl: MovieRepositoryImpl = mock()
    private var movieService: MovieService = mock()
    private var movieDao: MovieDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @ExperimentalCoroutinesApi
    @Before
    fun `setup depends`() {
        movieRepositoryImpl =
            MovieRepositoryImpl(testCoroutineContextProvider, movieService, movieDao)
    }

    @Test
    fun `get movies and not empty`() = coroutinesRule.runBlockingTest {
        // mock
        val mockMovies = MockUtil.getMockMovie()
        movieService.stub { onBlocking { getPopular() } doReturn MockUtil.getMockMovie() }

        // test
        val flow = movieRepositoryImpl.getFakePopularMovies()

        // verify
        flow.collect {
            it.isSuccess.shouldBeTrue()
            mockMovies.results `should be equal to` it.getOrNull()?.results
        }
    }

    @Test
    fun `get movies but server is error`() = coroutinesRule.runBlockingTest {
        // mock
        val expectedErrorServer = MockUtil.getServerError()
        movieService.stub {
            onBlocking { getPopular() } doAnswer {
                throw IOException(
                    expectedErrorServer
                )
            }
        }

        // test
        val flow = movieRepositoryImpl.getFakePopularMovies()

        // verify
        flow.collect {
            it.isFailure.shouldBeTrue()
            expectedErrorServer `should be equal to` it.exceptionOrNull()?.message
        }
    }

    @Test
    fun `get movie and not empty`() = coroutinesRule.runBlockingTest {
        // mock
        val mockMovie = MockUtil.mockMovieEnt()
        movieDao.stub { onBlocking { getObject(0) } doReturn mockMovie }

        // test
        val flow = movieRepositoryImpl.getFakeLocalMovie(0)

        // verify
        flow.collect {
            it.isSuccess.shouldBeTrue()
            mockMovie `should be equal to` it.getOrNull()
        }
    }

    @Test
    fun `get movie but database is empty`() = coroutinesRule.runBlockingTest {
        // mock
        val expectedErrorDatabase = MockUtil.getCacheError()
        movieDao.stub {
            onBlocking { getObject(0) } doAnswer {
                throw IOException(
                    expectedErrorDatabase
                )
            }
        }

        // test
        val flow = movieRepositoryImpl.getFakeLocalMovie(0)

        // verify
        flow.collect {
            it.isFailure.shouldBeTrue()
            expectedErrorDatabase `should be equal to` it.exceptionOrNull()?.message
        }
    }
}

// Source: https://medium.com/@heyitsmohit/unit-testing-delays-errors-retries-with-kotlin-flows-77ce00d0c2f3
// Source: https://proandroiddev.com/eliminating-coroutine-leaks-in-tests-3af825e7cde2