package com.corp.luqman.movielisting.di.hilt

import android.content.Context
import androidx.room.Room
import com.corp.luqman.movielisting.data.database.MovieDao
import com.corp.luqman.movielisting.data.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideWeatherDao(movieDatabase: MovieDatabase): MovieDao
            = movieDatabase.movieDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase
            = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        "movies_database")
        .fallbackToDestructiveMigration()
        .build()
}