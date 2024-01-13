package com.corp.luqman.movielisting.data.repository

import com.corp.luqman.movielisting.data.database.MovieDao
import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.data.remote.ApiService
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiService: ApiService, private val dao: MovieDao) {

    fun getMoviesData(movie:String, page:String, language:String)
            = apiService.getListMovies(movie, page, language)
    fun searchMovie(page:String, language:String, keyword: String, adult_content: String)
            = apiService.searchMovieByKeyWord(page, language, keyword, adult_content)

    fun getDetailMovieData(id:String, language:String) = apiService.getMovieDetail(id, language)

    fun getReviewsMovie(id:String, page:String, language:String) = apiService.getMovieReview(id, page, language)

//    fun getDataGenres(language:String) = apiService.getDataGenre(language)

    fun getVideos(id:String, language:String) = apiService.getDataVideo(id, language)

    suspend fun getFavorites() = dao.getFavorites()

    suspend fun searchFavorite(key: String) = dao.searchFavorite("%$key%")

    suspend fun getFavById(id: String) = dao.getFavById(id)

    suspend fun insertFavorite(favorite: Favorite) = dao.insertFavorite(favorite)

    suspend fun deleteAllFavorites() = dao.deleteAllFavorites()

    suspend fun deleteFavorite(favorite: Favorite) = dao.deleteFavorite(favorite)


}