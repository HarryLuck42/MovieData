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
import com.corp.luqman.movielisting.databinding.MovieUpcomingFragmentBinding
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
class MoviesUpcomingFragment : Fragment() {
    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var searchMovieDialog : MaterialDialog

    private val upViewModel: MoviesUpcomingViewModel by viewModels()

    private lateinit var adapter : MovieAdapter

    private lateinit var binding: MovieUpcomingFragmentBinding

    private var paging = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieUpcomingFragmentBinding.inflate(inflater)
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(binding.root.context, getString(R.string.loading))
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMoviesUp.layoutManager = layoutManager
        binding.rvMoviesUp.setHasFixedSize(true)
        binding.rvMoviesUp.isFocusable = false
        binding.rvMoviesUp.addItemDecoration(GridSpacingItemDecoration(2, 16, false))
        binding.rvMoviesUp.visibility = View.VISIBLE
        binding.tvNotFoundMovieUp.visibility = View.GONE
        binding.ivNotFoundUp.visibility = View.GONE
        adapter = MovieAdapter(
            this.requireContext(),
            upViewModel.listMovie, MovieListener(clickListener = { id ->
                upViewModel.onMovieClicked(id)
            }, clickFavorite = {
                upViewModel.saveFavorite(it)
                val index = upViewModel.listMovie.indexOf(it)
                val isFav = upViewModel.listMovie[index].isFavorite
                upViewModel.listMovie[index].isFavorite = !isFav
                adapter.notifyItemChanged(index)
            })
        )
        adapter.notifyDataSetChanged()
        binding.rvMoviesUp.adapter = adapter
        upViewModel.navigateToDetail.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    MoviesUpcomingFragmentDirections.actionMovieUpcomingFragmentToDetailMovieFragment(
                        it
                    )
                )
                upViewModel.onMovieNavigated()
            }
        }
        initObserver(binding)
        initObserverSearchMovie(binding)
        onScrollAdapter(binding)
        upViewModel.inputSearchState(false)
        upViewModel.getListData(paging.toString(), Const.language)
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
        upViewModel.clearListMovie()
        upViewModel.inputSearchState(false)
        upViewModel.getListData(paging.toString(), Const.language)
    }

    private fun initObserver(binding : MovieUpcomingFragmentBinding){
        upViewModel.movieState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if (paging > it.data.totalPages!!) {
                        upViewModel.stopLoading()
                    } else {
                        upViewModel.startLoading()
                    }
                    if (tempPagging == 1) {
                        if (it.data.results!!.isEmpty()) {
                            binding.rvMoviesUp.visibility = View.GONE
                            binding.ivNotFoundUp.visibility = View.VISIBLE
                            binding.tvNotFoundMovieUp.visibility = View.VISIBLE
                        } else {
                            binding.rvMoviesUp.visibility = View.VISIBLE
                            binding.ivNotFoundUp.visibility = View.GONE
                            binding.tvNotFoundMovieUp.visibility = View.GONE

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

        upViewModel.favorites.observe(viewLifecycleOwner){
            if(it != null){
                upViewModel.listMovie.forEachIndexed{ i, movie ->
                    for(value in it){
                        if(value.id == movie.id){
                            upViewModel.listMovie[i].isFavorite = true
                        }
                    }
                }
            }
        }
    }

    private fun initObserverSearchMovie(binding : MovieUpcomingFragmentBinding){
        upViewModel.searchMovieState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if (paging > it.data.totalPages!!) {
                        upViewModel.stopLoading()
                    } else {
                        upViewModel.startLoading()
                    }
                    if (tempPagging == 1) {
                        if (it.data.results!!.isEmpty()) {
                            binding.rvMoviesUp.visibility = View.GONE
                            binding.ivNotFoundUp.visibility = View.VISIBLE
                            binding.tvNotFoundMovieUp.visibility = View.VISIBLE
                        } else {
                            binding.rvMoviesUp.visibility = View.VISIBLE
                            binding.ivNotFoundUp.visibility = View.GONE
                            binding.tvNotFoundMovieUp.visibility = View.GONE
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



    private fun onScrollAdapter(binding : MovieUpcomingFragmentBinding){

        binding.rvMoviesUp.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (binding.rvMoviesUp.layoutManager!! as GridLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (binding.rvMoviesUp.layoutManager!! as GridLayoutManager).itemCount
                    if (upViewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        upViewModel.stopLoading()
                        if(upViewModel.isSearchState.value!!){
                            upViewModel.searchMovieByKeyword(paging.toString(), Const.language,
                                upViewModel.keywordValue.value)
                        }else{
                            upViewModel.getListData(paging.toString(), Const.language)
                        }
                        progressDialog.show()
                    }

                }
            }
        })
    }

    private fun settingDialogSearch(v : View){
        val bindingDialog = SearchMovieDialogBinding.inflate(LayoutInflater.from(v.context))
        searchMovieDialog = Helpers.customViewDialog(v.context, R.layout.search_movie_dialog, bindingDialog, true)
        var message = ""
        bindingDialog.etKeywordSearch.requestFocus()
        bindingDialog.btnSearch.setOnClickListener {
            val keyword = bindingDialog.etKeywordSearch.text.toString()
            if(keyword.isEmpty()){
                message += "Please input keyword search !"
            }else{
                upViewModel.inputKeyword(keyword)
            }
            if(message.isEmpty()){
                paging = 1
                upViewModel.clearListMovie()
                upViewModel.inputSearchState(true)
                upViewModel.searchMovieByKeyword(paging.toString(), Const.language,
                    upViewModel.keywordValue.value)
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