package com.corp.luqman.movielisting.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corp.luqman.movielisting.data.database.MovieDao
import com.corp.luqman.movielisting.data.database.MovieDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.movieDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInsertFavorite() = runTest {

    }


}