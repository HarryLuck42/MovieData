package com.corp.luqman.movielisting.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corp.luqman.movielisting.data.database.MovieDao
import com.corp.luqman.movielisting.data.database.MovieDatabase
import com.corp.luqman.movielisting.data.models.Favorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MovieDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.movieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun testInsertFavorite() = runTest {
        val movie = Favorite(
            753342,
            2141.546,
            1069,
            false,
            "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
            false,
            "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
            "en",
            "Napoleon",
            mutableListOf(
                36,
                10752,
                18
            ),
            "Napoleon",
            6.457,
            "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
            "2023-11-22"
        )
        dao.insertFavorite(movie)
        val favorites = dao.getFavorites()
        assertThat(favorites).contains(movie)
    }

    @Test
    fun testDeleteFavorite() = runTest {
        val movie = Favorite(
            753342,
            2141.546,
            1069,
            false,
            "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
            false,
            "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
            "en",
            "Napoleon",
            mutableListOf(
                36,
                10752,
                18
            ),
            "Napoleon",
            6.457,
            "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
            "2023-11-22"
        )
        dao.insertFavorite(movie)
        dao.deleteFavorite(movie)
        val favorites = dao.getFavorites()
        assertThat(favorites).doesNotContain(movie)
    }


    @Test
    fun testSearchFavoriteNotFound() = runTest {
        val movies = mutableListOf(
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Napoleon",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Hunger Games",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Harry Potter",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Oppenheimer",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Barbie",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
        )
        for (movie in movies){
            dao.insertFavorite(movie)
        }
        val favorites = dao.searchFavorite("%Aquaman%")
        assertThat(favorites).containsNoneIn(movies)
    }

    @Test
    fun testSearchFavoriteFound() = runTest {
        val movies = mutableListOf(
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Napoleon",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Hunger Games",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Harry Potter",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Oppenheimer",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
            Favorite(
                753342,
                2141.546,
                1069,
                false,
                "/jE5o7y9K6pZtWNNMEw3IdpHuncR.jpg",
                false,
                "/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                "en",
                "Napoleon",
                mutableListOf(
                    36,
                    10752,
                    18
                ),
                "Barbie",
                6.457,
                "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
                "2023-11-22"
            ),
        )
        for (movie in movies){
            dao.insertFavorite(movie)
        }
        val favorites = dao.searchFavorite("%Barb%")
        assertThat(favorites).isNotEmpty()
    }
}