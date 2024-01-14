package com.corp.luqman.movielisting.data.remote

import com.corp.luqman.movielisting.data.models.response.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/{movie}")
    fun getListMovies(
        @Path("movie") movie : String,
        @Query("page")page:String,
        @Query("language")language:String):Deferred<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetail(
        @Path("id") id : String,
        @Query("language")language:String):Deferred<MovieDetailResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReview(
        @Path("id") id : String,
        @Query("page")page:String,
        @Query("language")language:String):Deferred<ReviewMovieResponse>

    @GET("movie/{id}/videos")
    fun getDataVideo(
        @Path("id") id : String,
        @Query("language")language:String):Deferred<VideoResponse>

    @GET("search/movie")
    fun searchMovieByKeyWord(
        @Query("page")page:String,
        @Query("language")language:String,
        @Query("query")keyword:String,
        @Query("include_adult")include_adult:String):Deferred<MoviesResponse>
}