package com.corp.luqman.movielisting.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.databinding.DetailMovieFragmentBinding
import com.corp.luqman.movielisting.databinding.WatchTrailerDialogBinding
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.NetworkHelper
import com.corp.luqman.movielisting.utils.UiState
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var watchYoutubeDialog : MaterialDialog

    private val viewModel: DetailMovieViewModel by viewModels()

    private lateinit var binding: DetailMovieFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailMovieFragmentBinding.inflate(inflater)
        val v = binding.root
        binding.layoutDetailMovie.visibility = View.GONE
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(v.context, getString(R.string.loading))
        initObserver(v)
        initObserverVideos(v)
        val arguments = DetailMovieFragmentArgs.fromBundle(requireArguments())
        viewModel.getDetailMovie(arguments.idMovie.toString(), Const.language)
        viewModel.videos.observe(viewLifecycleOwner){
            val videoNameList : MutableList<String> = mutableListOf()
            for(value in it){
                if(value.type!!.contains("Trailer") && value.site!!.contains("YouTube")){
                    videoNameList.add(value.name!!)
                }
            }
            binding.btnTrailer.setOnClickListener { view ->
                val dialogChooseVideo = AlertDialog.Builder(v.context)
                val dataAdapter = ArrayAdapter(v.context, R.layout.support_simple_spinner_dropdown_item, videoNameList)
                dialogChooseVideo.setAdapter(dataAdapter) { dialog, which ->
                    settingDialogWatchTrailer(v, it[which].key!!)
                }
                val dialog = dialogChooseVideo.create()
                dialog.show()
            }
        }
        viewModel.movieDetail.observe(viewLifecycleOwner){
            it.let {
                binding.layoutDetailMovie.visibility = View.VISIBLE
                binding.tvTitleHeader.text = it.title
                binding.tvRate.text = it.voteAverage.toString()
                binding.tvRelease.text = it.releaseDate.toString()
                var genres =""
                for(index in it.genres!!.indices){
                    if (index == 0){
                        genres += it.genres!!.get(index).desc
                    }else{
                        genres += ", " + it.genres!!.get(index).desc
                    }
                }
                binding.tvGenre.text = genres
                binding.tvVote.text = it.voteCount.toString()
                binding.tvPopularity.text = it.popularity.toString()
                var productions = ""
                for(index in it.productionCompanies!!.indices){
                    if (index == 0){
                        productions += it.productionCompanies!!.get(index).name
                    }else{
                        productions += ", " + it.productionCompanies!!.get(index).name
                    }
                }
                binding.tvProduction.text = productions
                var countries = ""
                for(index in it.productionCountries!!.indices){
                    if (index == 0){
                        countries += it.productionCountries!!.get(index).name
                    }else{
                        countries += ", " + it.productionCountries!!.get(index).name
                    }
                }
                binding.tvCountry.text = countries
                val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
                val budget: String = format.format(it.budget)
                binding.tvBudget.text = budget
                binding.tvOverview.text = it.overview
                binding.tvHomepage.text = it.homepage
                Glide.with(v.context).load(Const.imageUrlbase + it.posterPath).apply(
                    RequestOptions().placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken)).into(binding.ivMovieDetail)
                val idmovie = it.id
                binding.btnReview.setOnClickListener {
                    viewModel.onMovieClicked(idmovie!!)
                }

            }

        }

        viewModel.navigateToReview.observe(viewLifecycleOwner){
            it?.let {
                this.findNavController().navigate(DetailMovieFragmentDirections.actionDetailMovieFragmentToReviewMovieFragment(it))
                viewModel.onMovieNavigated()
            }
        }
        return v
    }



    private fun settingDialogWatchTrailer(v : View, videoId : String){
        val binding = WatchTrailerDialogBinding.inflate(LayoutInflater.from(context))
        watchYoutubeDialog = Helpers.customViewDialog(v.context, R.layout.watch_trailer_dialog, binding, true)
        lifecycle.addObserver(binding.playTrailer)
        binding.playTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })
        watchYoutubeDialog.show {
            cancelable(true)
        }
    }
    private fun initObserver(v : View){
        viewModel.detailState.observe(viewLifecycleOwner){
            when (it){
                is UiState.Loading -> {
                    progressDialog.show()
                }
                is UiState.Success -> {
                    progressDialog.dismiss()
                    viewModel.getDataVideo(it.data.id.toString(), Const.apikey, Const.language)
                }
                is UiState.Error -> {
                    progressDialog.dismiss()
                    val message = NetworkHelper().getErrorMessage(it.throwable)
                    Helpers.showGeneralOkDialog(
                        v.context,
                        getString(R.string.perhatian),
                        message
                    )
                }
                else -> {

                }
            }
        }
    }

    private fun initObserverVideos(v : View){
        viewModel.videoState.observe(viewLifecycleOwner){
            when (it){
                is UiState.Loading -> {
                    progressDialog.show()
                }
                is UiState.Success -> {
                    progressDialog.dismiss()
                }
                is UiState.Error -> {
                    progressDialog.dismiss()
                    val message = NetworkHelper().getErrorMessage(it.throwable)
                    Helpers.showGeneralOkDialog(
                        v.context,
                        getString(R.string.perhatian),
                        message
                    )
                }
                else -> {

                }
            }
        }
    }





}