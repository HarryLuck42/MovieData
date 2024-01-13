package com.corp.luqman.movielisting.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(private val repository: MoviesRepository): ViewModel() {



    private val _favorites = MutableLiveData<MutableList<Favorite>>()
    val favorites
        get() = _favorites

    fun getFavorites() {
        viewModelScope.launch {
            favorites.value = repository.getFavorites()
        }
    }

    fun searchFavorites(keyword: String){
        viewModelScope.launch {
            favorites.value = repository.searchFavorite(keyword)
        }
    }

    fun saveFavorite(movie: Favorite){
        viewModelScope.launch {
            repository.deleteFavorite(movie)
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
}