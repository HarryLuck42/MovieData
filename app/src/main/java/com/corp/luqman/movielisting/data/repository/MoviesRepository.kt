package com.corp.luqman.movielisting.data.repository

import com.corp.luqman.movielisting.data.database.MovieDao
import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.data.remote.ApiService
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiService: ApiService, private val dao: MovieDao) : MoviesRepositoryImpl {

    override suspend fun getFavorites() = dao.getFavorites()

    override suspend fun searchFavorite(title: String) = dao.searchFavorite("%$title%")

    override suspend fun insertFavorite(favorite: Favorite) = dao.insertFavorite(favorite)

    override suspend fun deleteFavorite(favorite: Favorite) = dao.deleteFavorite(favorite)
    override fun getListMovies(
        movie: String,
        page: String,
        language: String
    ) = apiService.getListMovies(movie, page, language)

    override fun getMovieDetail(id: String, language: String) = apiService.getMovieDetail(id, language)

    override fun getMovieReview(
        id: String,
        page: String,
        language: String
    ) = apiService.getMovieReview(id, page, language)

    override fun getDataVideo(id: String, language: String) = apiService.getDataVideo(id, language)

    override fun searchMovieByKeyWord(
        page: String,
        language: String,
        keyword: String,
        include_adult: String
    ) = apiService.searchMovieByKeyWord(page, language, keyword, include_adult)


}