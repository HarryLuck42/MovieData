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
import com.corp.luqman.movielisting.databinding.MoviesFragmentBinding
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
class MoviesPopularFragment : Fragment() {

    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var searchMovieDialog : MaterialDialog

    private val popularPopularViewModel: MoviesPopularViewModel by viewModels()

    private lateinit var adapter : MovieAdapter

    private var paging = 1

    private lateinit var binding : MoviesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesFragmentBinding.inflate(inflater)
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(binding.root.context, getString(R.string.loading))
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMovies.layoutManager = layoutManager
        binding.rvMovies.setHasFixedSize(true)
        binding.rvMovies.isFocusable = false
        binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(2, 16, false))
        binding.rvMovies.visibility = View.VISIBLE
        binding.tvNotFoundMovie.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
        adapter = MovieAdapter(
            this.requireContext(),
            popularPopularViewModel.listMovie, MovieListener(clickListener = { id ->
                popularPopularViewModel.onMovieClicked(id)
            }, clickFavorite = {
                popularPopularViewModel.saveFavorite(it)
                val index = popularPopularViewModel.listMovie.indexOf(it)
                val isFav = popularPopularViewModel.listMovie[index].isFavorite
                if(isFav){
                    popularPopularViewModel.listMovie[index].isFavorite = false
                }else{
                    popularPopularViewModel.listMovie[index].isFavorite = true
                }
                adapter.notifyItemChanged(index)
            })
        )
        adapter.notifyDataSetChanged()
        binding.rvMovies.adapter = adapter
        popularPopularViewModel.navigateToDetail.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    MoviesPopularFragmentDirections.actionMoviesFragmentToDetailMovieFragment(it)
                )
                popularPopularViewModel.onMovieNavigated()
            }
        }
        initObserver(binding)
        initObserverSearchMovie(binding)
        onScrollAdapter(binding)
        popularPopularViewModel.inputSearchState(false)
        popularPopularViewModel.getListData(paging.toString(), Const.language,)
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
        popularPopularViewModel.clearListMovie()
        popularPopularViewModel.inputSearchState(false)
        popularPopularViewModel.getListData(paging.toString(), Const.language)
    }

    private fun initObserver(binding : MoviesFragmentBinding){
        popularPopularViewModel.movieState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if (paging > it.data.totalPages!!) {
                        popularPopularViewModel.stopLoading()
                    } else {
                        popularPopularViewModel.startLoading()
                    }
                    if (tempPagging == 1) {
                        if (it.data.results!!.isEmpty()) {
                            binding.rvMovies.visibility = View.GONE
                            binding.ivNotFound.visibility = View.VISIBLE
                            binding.tvNotFoundMovie.visibility = View.VISIBLE
                        } else {
                            binding.rvMovies.visibility = View.VISIBLE
                            binding.ivNotFound.visibility = View.GONE
                            binding.tvNotFoundMovie.visibility = View.GONE

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

        popularPopularViewModel.favorites.observe(viewLifecycleOwner){
            if(it != null){
                popularPopularViewModel.listMovie.forEachIndexed{ i, movie ->
                    for(value in it){
                        if(value.id == movie.id){
                            popularPopularViewModel.listMovie[i].isFavorite = true
                        }
                    }
                }
            }
        }
    }

    private fun initObserverSearchMovie(binding : MoviesFragmentBinding){
        popularPopularViewModel.searchMovieState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if (paging > it.data.totalPages!!) {
                        popularPopularViewModel.stopLoading()
                    } else {
                        popularPopularViewModel.startLoading()
                    }
                    if (tempPagging == 1) {
                        if (it.data.results!!.isEmpty()) {
                            binding.rvMovies.visibility = View.GONE
                            binding.ivNotFound.visibility = View.VISIBLE
                            binding.tvNotFoundMovie.visibility = View.VISIBLE
                        } else {
                            binding.rvMovies.visibility = View.VISIBLE
                            binding.ivNotFound.visibility = View.GONE
                            binding.tvNotFoundMovie.visibility = View.GONE
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

    private fun onScrollAdapter(binding : MoviesFragmentBinding){

        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (binding.rvMovies.layoutManager!! as GridLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (binding.rvMovies.layoutManager!! as GridLayoutManager).itemCount
                    if (popularPopularViewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        popularPopularViewModel.stopLoading()
                        if(popularPopularViewModel.isSearchState.value!!){
                            popularPopularViewModel.searchMovieByKeyword(paging.toString(), Const.language,
                            popularPopularViewModel.keywordValue.value)
                        }else{
                            popularPopularViewModel.getListData(paging.toString(), Const.language)
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
                popularPopularViewModel.inputKeyword(keyword)
            }
            if(message.isEmpty()){
                paging = 1
                popularPopularViewModel.clearListMovie()
                popularPopularViewModel.inputSearchState(true)
                popularPopularViewModel.searchMovieByKeyword(paging.toString(), Const.language,
                    popularPopularViewModel.keywordValue.value)
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