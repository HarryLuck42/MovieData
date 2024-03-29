package com.corp.luqman.movielisting.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corp.luqman.movielisting.data.models.Review
import com.corp.luqman.movielisting.data.models.response.ReviewMovieResponse
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ReviewMovieViewModel @Inject constructor(val moviesRepository: MoviesRepository,) : ViewModel() {
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
    val reviewState = MutableLiveData<UiState<ReviewMovieResponse>>()

    val listReview : MutableList<Review> = mutableListOf()

    fun getListReview(id: String, page:String, language:String){
        reviewState.value = UiState.Loading()

        viewModelScope.launch {
            try {
                val result = moviesRepository.getMovieReview(id, page, language).await()

                listReview.addAll(result.results!!)


                reviewState.postValue(UiState.Success(result))


            }catch (e: Exception){
                reviewState.postValue(UiState.Error(e))
            }
        }
    }


}