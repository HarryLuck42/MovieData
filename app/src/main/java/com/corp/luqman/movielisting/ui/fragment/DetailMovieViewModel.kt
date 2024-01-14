package com.corp.luqman.movielisting.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corp.luqman.movielisting.data.models.Video
import com.corp.luqman.movielisting.data.models.response.MovieDetailResponse
import com.corp.luqman.movielisting.data.models.response.VideoResponse
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {


    val detailState = MutableLiveData<UiState<MovieDetailResponse>>()
    val videoState = MutableLiveData<UiState<VideoResponse>>()

    private val _videos = MutableLiveData<MutableList<Video>>()
    val videos : LiveData<MutableList<Video>>
        get() = _videos

    private val _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail : LiveData<MovieDetailResponse>
        get() = _movieDetail

    fun getDetailMovie(id:String, language:String){
        detailState.value = UiState.Loading()

        viewModelScope.launch {
            try {
                val result = moviesRepository.getMovieDetail(id, language).await()
                _movieDetail.postValue(result)
                detailState.postValue(UiState.Success(result))

            }catch (e: Exception){
                detailState.postValue(UiState.Error(e))
            }
        }
    }

    fun getDataVideo(id:String, language:String){
        videoState.value = UiState.Loading()

        viewModelScope.launch {
            try {
                val result = moviesRepository.getDataVideo(id, language).await()
                result.results?.let {
                    _videos.postValue(it)
                    videoState.postValue(UiState.Success(result))
                }


            }catch (e: Exception){
                videoState.postValue(UiState.Error(e))
            }
        }
    }

    private val _navigateToReview = MutableLiveData<Long?>()
    val navigateToReview
        get() = _navigateToReview

    fun onMovieClicked(id: Long) {
        _navigateToReview.value = id
    }

    fun onMovieNavigated(){
        _navigateToReview.value = null
    }
}