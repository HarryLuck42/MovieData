package com.corp.luqman.movielisting.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.databinding.MovieNowPlayingFragmentBinding
import com.corp.luqman.movielisting.databinding.SearchMovieDialogBinding
import com.corp.luqman.movielisting.ui.adapter.MovieAdapter
import com.corp.luqman.movielisting.ui.adapter.MovieListener
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.NetworkHelper
import com.corp.luqman.movielisting.utils.UiState
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import com.corp.luqman.movielisting.utils.custom.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("NotifyDataSetChanged")
@AndroidEntryPoint
class MoviesNowPlayingFragment : Fragment() {
    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var searchMovieDialog : MaterialDialog

    private val nowViewModel: MoviesNowPlayingViewModel by viewModels()

    private lateinit var adapter : MovieAdapter

    private var paging = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MovieNowPlayingFragmentBinding.inflate(inflater)
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(binding.root.context, getString(R.string.loading))
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMoviesNow.layoutManager = layoutManager
        binding.rvMoviesNow.setHasFixedSize(true)
        binding.rvMoviesNow.isFocusable = false
        binding.rvMoviesNow.addItemDecoration(GridSpacingItemDecoration(2, 16, false))
        binding.rvMoviesNow.visibility = View.VISIBLE
        binding.tvNotFoundMovieNow.visibility = View.GONE
        binding.ivNotFoundNow.visibility = View.GONE
        adapter = MovieAdapter(
            this.requireContext(),
            nowViewModel.listMovie, MovieListener(clickListener = { id ->
                nowViewModel.onMovieClicked(id)
            }, clickFavorite = {
                nowViewModel.saveFavorite(it)
                val index = nowViewModel.listMovie.indexOf(it)
                val isFav = nowViewModel.listMovie[index].isFavorite
                if(isFav){
                    nowViewModel.listMovie[index].isFavorite = false
                }else{
                    nowViewModel.listMovie[index].isFavorite = true
                }
                adapter.notifyItemChanged(index)
            })
        )
        adapter.notifyDataSetChanged()
        binding.rvMoviesNow.adapter = adapter
        nowViewModel.navigateToDetail.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    MoviesNowPlayingFragmentDirections.actionMovieNowPlayingFragmentToDetailMovieFragment(
                        it
                    )
                )
                nowViewModel.onMovieNavigated()
            }
        }
        initObserver(binding)
        initObserverSearchMovie(binding)
        onScrollAdapter(binding)
        nowViewModel.inputSearchState(false)
        nowViewModel.getListData(paging.toString(), Const.language,)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search_movies->{
                settingDialogSearch(this.requireView())
            }
            R.id.list_default->{
                refreshListMovie()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshListMovie() {
        paging = 1
        nowViewModel.clearListMovie()
        nowViewModel.inputSearchState(false)
        nowViewModel.getListData( paging.toString(), Const.language)
    }

    private fun initObserver(binding : MovieNowPlayingFragmentBinding){
        nowViewModel.movieState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if (paging > it.data.totalPages!!) {
                        nowViewModel.stopLoading()
                    } else {
                        nowViewModel.startLoading()
                    }
                    if (tempPagging == 1) {
                        if (it.data.results!!.isEmpty()) {
                            binding.rvMoviesNow.visibility = View.GONE
                            binding.ivNotFoundNow.visibility = View.VISIBLE
                            binding.tvNotFoundMovieNow.visibility = View.VISIBLE
                        } else {
                            binding.rvMoviesNow.visibility = View.VISIBLE
                            binding.ivNotFoundNow.visibility = View.GONE
                            binding.tvNotFoundMovieNow.visibility = View.GONE

                        }
                    }



                    adapter.notifyDataSetChanged()

                    progressDialog.dismiss()
                }
                is UiState.Error -> {
                    progressDialog.dismiss()
                    val message = NetworkHelper().getErrorMessage(it.throwable)
                    Helpers.showGeneralOkDialog(
                        binding.root.context,
                        getString(R.string.perhatian),
                        message
                    )
                }
                else -> {

                }
            }
        }

        nowViewModel.favorites.observe(viewLifecycleOwner){
            if(it != null){
                checkFavorite(it)
            }
        }
    }

    private fun checkFavorite(values: MutableList<Favorite>){
        nowViewModel.listMovie.forEachIndexed{ i, movie ->
            for(value in values){
                if(value.id == movie.id){
                    nowViewModel.listMovie[i].isFavorite = true
                }
            }
        }
    }

    private fun initObserverSearchMovie(binding: MovieNowPlayingFragmentBinding){
        nowViewModel.searchMovieState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if (paging > it.data.totalPages!!) {
                        nowViewModel.stopLoading()
                    } else {
                        nowViewModel.startLoading()
                    }
                    if (tempPagging == 1) {
                        if (it.data.results!!.isEmpty()) {
                            binding.rvMoviesNow.visibility = View.GONE
                            binding.ivNotFoundNow.visibility = View.VISIBLE
                            binding.tvNotFoundMovieNow.visibility = View.VISIBLE
                        } else {
                            binding.rvMoviesNow.visibility = View.VISIBLE
                            binding.ivNotFoundNow.visibility = View.GONE
                            binding.tvNotFoundMovieNow.visibility = View.GONE
                        }
                    }

                    adapter.notifyDataSetChanged()

                    progressDialog.dismiss()
                }
                is UiState.Error -> {
                    progressDialog.dismiss()
                    val message = NetworkHelper().getErrorMessage(it.throwable)
                    Helpers.showGeneralOkDialog(
                        binding.root.context,
                        getString(R.string.perhatian),
                        message
                    )
                }
                else -> {

                }
            }
        }
    }



    private fun onScrollAdapter(binding : MovieNowPlayingFragmentBinding){

        binding.rvMoviesNow.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (binding.rvMoviesNow.layoutManager!! as GridLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (binding.rvMoviesNow.layoutManager!! as GridLayoutManager).itemCount
                    if (nowViewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        nowViewModel.stopLoading()
                        if(nowViewModel.isSearchState.value!!){
                            nowViewModel.searchMovieByKeyword(paging.toString(), Const.language,
                                nowViewModel.keywordValue.value)
                        }else{
                            nowViewModel.getListData(paging.toString(), Const.language)
                        }
                        progressDialog.show()
                    }

                }
            }
        })
    }

    private fun settingDialogSearch(v : View){
        val binding = SearchMovieDialogBinding.inflate(LayoutInflater.from(v.context))
        searchMovieDialog = Helpers.customViewDialog(v.context, R.layout.search_movie_dialog, binding, true)
        var message = ""
        binding.etKeywordSearch.requestFocus()
        binding.btnSearch.setOnClickListener {
            val keyword = binding.etKeywordSearch.text.toString()
            if(keyword.isEmpty()){
                message += "Please input keyword search !"
            }else{
                nowViewModel.inputKeyword(keyword)
            }
            if(message.isEmpty()){
                paging = 1
                nowViewModel.clearListMovie()
                nowViewModel.inputSearchState(true)
                nowViewModel.searchMovieByKeyword(paging.toString(), Const.language,
                    nowViewModel.keywordValue.value)
                searchMovieDialog.dismiss()
            }else{
                Helpers.showGeneralOkDialog(v.context, "Warning", message)
            }
        }

        searchMovieDialog.show {
            cancelable(true)
        }
    }


}