package com.corp.luqman.movielisting.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.data.models.Movie
import com.corp.luqman.movielisting.data.models.response.MoviesResponse
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MoviesNowPlayingViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {
    var isLoading = false

    fun stopLoading() {
        isLoading = false
    }

    fun startLoading(){
        isLoading = true
    }
    init {
        stopLoading()
    }

    val movieState = MutableLiveData<UiState<MoviesResponse>>()
    val searchMovieState = MutableLiveData<UiState<MoviesResponse>>()

    val listMovie : MutableList<Movie> = mutableListOf()

    fun clearListMovie(){
        listMovie.clear()
    }

    private val _favorites = MutableLiveData<MutableList<Favorite>>()
    val favorites
        get() = _favorites

    suspend fun getFavorites() {
        favorites.value = moviesRepository.getFavorites()

    }

    fun getListData(page:String, language:String){
        movieState.value = UiState.Loading()

        viewModelScope.launch {
            try {
                val result = moviesRepository.getMoviesData(Const.NOW_PLAYING_PATH, page, language).await()

                listMovie.addAll(result.results!!)

                getFavorites()
                movieState.postValue(UiState.Success(result))


            }catch (e: Exception){
                movieState.postValue(UiState.Error(e))
            }
        }
    }

    fun searchMovieByKeyword(page:String, language:String,
                             keyword: String?){
        searchMovieState.value = UiState.Loading()

        viewModelScope.launch {
            try {
                val result = moviesRepository.searchMovie(page, language,
                    keyword!!, "false").await()

                listMovie.addAll(result.results!!)


                searchMovieState.postValue(UiState.Success(result))


            }catch (e: Exception){
                searchMovieState.postValue(UiState.Error(e))
            }
        }
    }

    fun saveFavorite(movie: Movie){
        viewModelScope.launch {
            if(movie.isFavorite){
                moviesRepository.deleteFavorite(movie.convert())
            }else{
                moviesRepository.insertFavorite(movie.convert())
            }
            getFavorites()
        }
    }

    private val _navigateToDetail = MutableLiveData<Long?>()
    val navigateToDetail
        get() = _navigateToDetail

    fun onMovieClicked(id: Long) {
        _navigateToDetail.value = id
    }

    fun onMovieNavigated(){
        _navigateToDetail.value = null
    }

    private val _keywordValue = MutableLiveData<String?>()
    val keywordValue
        get() = _keywordValue

    fun inputKeyword(value: String) {
        _keywordValue.value = value
    }

    private val _isSearchState = MutableLiveData<Boolean>()
    val isSearchState
        get() = _isSearchState

    fun inputSearchState(value: Boolean) {
        _isSearchState.value = value
    }
}