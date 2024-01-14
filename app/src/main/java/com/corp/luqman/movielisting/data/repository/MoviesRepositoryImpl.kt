package com.corp.luqman.movielisting.data.repository

import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.data.models.response.MovieDetailResponse
import com.corp.luqman.movielisting.data.models.response.MoviesResponse
import com.corp.luqman.movielisting.data.models.response.ReviewMovieResponse
import com.corp.luqman.movielisting.data.models.response.VideoResponse
import kotlinx.coroutines.Deferred

interface MoviesRepositoryImpl {

    suspend fun getFavorites(): MutableList<Favorite>?

    suspend fun searchFavorite(title: String): MutableList<Favorite>?

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    fun getListMovies(
        movie : String,
        page:String,
        language:String): Deferred<MoviesResponse>

    fun getMovieDetail(
        id : String,
        language:String):Deferred<MovieDetailResponse>

    fun getMovieReview(
        id : String,
        page:String,
        language:String):Deferred<ReviewMovieResponse>

    fun getDataVideo(
        id : String,
        language:String):Deferred<VideoResponse>

    fun searchMovieByKeyWord(
        page:String,
        language:String,
        keyword:String,
        include_adult:String):Deferred<MoviesResponse>
}