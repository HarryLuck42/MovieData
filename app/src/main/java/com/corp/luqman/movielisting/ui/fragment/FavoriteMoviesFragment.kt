package com.corp.luqman.movielisting.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.databinding.FragmentFavoriteMoviesBinding
import com.corp.luqman.movielisting.databinding.SearchMovieDialogBinding
import com.corp.luqman.movielisting.ui.adapter.FavoriteAdapter
import com.corp.luqman.movielisting.ui.adapter.FavoriteListener
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import com.corp.luqman.movielisting.utils.custom.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("NotifyDataSetChanged")
@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment() {
    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var searchMovieDialog : MaterialDialog

    private val favViewModel: FavoriteMoviesViewModel by viewModels()

    private lateinit var adapter : FavoriteAdapter

    private lateinit var binding : FragmentFavoriteMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMoviesBinding.inflate(inflater)
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(binding.root.context, getString(R.string.loading))
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMoviesFav.layoutManager = layoutManager
        binding.rvMoviesFav.setHasFixedSize(true)
        binding.rvMoviesFav.isFocusable = false
        binding.rvMoviesFav.addItemDecoration(GridSpacingItemDecoration(2, 16, false))
        binding.rvMoviesFav.visibility = View.VISIBLE
        binding.tvNotFoundMovieFav.visibility = View.GONE
        binding.ivNotFoundFav.visibility = View.GONE

        favViewModel.favorites.observe(viewLifecycleOwner){
            if(it != null){
                if(it.isEmpty()){
                    binding.rvMoviesFav.visibility = View.GONE
                    binding.tvNotFoundMovieFav.visibility = View.VISIBLE
                    binding.ivNotFoundFav.visibility = View.VISIBLE
                }else{
                    adapter = FavoriteAdapter(this.requireContext(), it, FavoriteListener(
                        clickListener = { id ->
                            favViewModel.onMovieClicked(id)
                        }, clickFavorite = {
                            favViewModel.saveFavorite(it)
                        }
                    ))
                    adapter.notifyDataSetChanged()
                    binding.rvMoviesFav.adapter = adapter
                }


            }else{
                binding.rvMoviesFav.visibility = View.GONE
                binding.tvNotFoundMovieFav.visibility = View.VISIBLE
                binding.ivNotFoundFav.visibility = View.VISIBLE
            }



        }
        favViewModel.navigateToDetail.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToDetailMovieFragment(
                        it
                    )
                )
                favViewModel.onMovieNavigated()
            }
        }
        favViewModel.getFavorites()
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
                favViewModel.getFavorites()
            }
        }
        return super.onOptionsItemSelected(item)
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
                favViewModel.searchFavorites(keyword)
            }
            if(message.isEmpty()){
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