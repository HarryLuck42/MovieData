package com.corp.luqman.movielisting.di.hilt

import com.corp.luqman.movielisting.data.database.MovieDao
import com.corp.luqman.movielisting.data.database.MovieDatabase
import com.corp.luqman.movielisting.data.remote.ApiService
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.rx.AppSchedulerProvider
import com.corp.luqman.movielisting.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppSchedulerProvider(): SchedulerProvider{
        return AppSchedulerProvider()
    }

//    @Singleton
//    @Provides
//    fun provideRepository(apiService: ApiService,
//                          dao: MovieDao): MoviesRepository{
//        return MoviesRepository(apiService, dao)
//    }
}