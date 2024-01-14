package com.corp.luqman.movielisting.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.corp.luqman.movielisting.data.remote.ApiService
import com.corp.luqman.movielisting.data.remote.TokenInterceptor
import com.corp.luqman.movielisting.utils.Const
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class ApiTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var api: ApiService

    @Before
    fun init(){
        val client = OkHttpClient.Builder()
        client.apply {
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
            addInterceptor(TokenInterceptor())
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logging)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(Const.appUrl)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        api = retrofit.create(ApiService::class.java)
    }

    @Test
    fun testGetPopularMovies() = runTest {
        val movies = api.getListMovies(Const.POPULAR_PATH, "1", Const.language).await().results
        assertThat(movies).isNotEmpty()
    }

    @Test
    fun testGetNowPlayingMovies() = runTest {
        val movies = api.getListMovies(Const.NOW_PLAYING_PATH, "1", Const.language).await().results
        assertThat(movies).isNotEmpty()
    }

    @Test
    fun testGetUpcomingMovies() = runTest {
        val movies = api.getListMovies(Const.UPCOMING_PATH, "1", Const.language).await().results
        assertThat(movies).isNotEmpty()
    }

    @Test
    fun testSearchVideo() = runTest {
        val movies = api.searchMovieByKeyWord("1", Const.language, "Aqua", "false").await().results
        assertThat(movies).isNotEmpty()
    }

    @Test
    fun testGetMovieDetail() = runTest {
        val movies = api.getListMovies(Const.POPULAR_PATH, "1", Const.language).await().results
        if(movies != null){
            val item = movies[0]
            val detail = api.getMovieDetail(item.id.toString(), Const.language).await()
            assertThat(detail.id).isEqualTo(item.id)
        }
    }

    @Test
    fun testGetMovieReview() = runTest {
        val movies = api.getListMovies(Const.POPULAR_PATH, "1", Const.language).await().results
        if(movies != null){
            val reviews = api.getMovieReview(movies[0].id.toString(), "1", Const.language).await().results
            assertThat(reviews).isNotEmpty()
        }
    }

    @Test
    fun testGetMovieTrailers() = runTest {
        val movies = api.getListMovies(Const.POPULAR_PATH, "1", Const.language).await().results
        if(movies != null){
            val reviews = api.getDataVideo(movies[0].id.toString(), Const.language).await().results
            assertThat(reviews).isNotEmpty()
        }
    }

}