package com.corp.luqman.movielisting.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.databinding.ReviewMovieFragmentBinding
import com.corp.luqman.movielisting.ui.adapter.ReviewAdapter
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.NetworkHelper
import com.corp.luqman.movielisting.utils.UiState
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("NotifyDataSetChanged")
@AndroidEntryPoint
class ReviewMovieFragment : Fragment() {

    private lateinit var progressDialog : CustomProgressDialog

    private val viewModel: ReviewMovieViewModel by viewModels()

    private var paging = 1

    private lateinit var adapter : ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ReviewMovieFragmentBinding.inflate(inflater)
        progressDialog = CustomProgressDialog(binding.root.context, getString(R.string.loading))
        setHasOptionsMenu(true)
        val layoutManager = LinearLayoutManager(this.requireContext(), GridLayoutManager.VERTICAL, false)
        binding.rvReviews.layoutManager = layoutManager
        binding.rvReviews.setHasFixedSize(true)
        binding.rvReviews.isFocusable = false
        binding.rvReviews.visibility = View.VISIBLE
        binding.tvNotFoundReview.visibility = View.GONE
        binding.ivNotFoundReview.visibility = View.GONE
        adapter = ReviewAdapter(
            this.requireContext(), viewModel.listReview)
        adapter.notifyDataSetChanged()
        binding.rvReviews.adapter = adapter
        val arguments = ReviewMovieFragmentArgs.fromBundle(requireArguments())
        viewModel.getListReview(arguments.idReview.toString(), paging.toString(), Const.language)
        initObserver(binding)
        onScrollAdapter(binding, arguments.idReview.toString())
        return binding.root
    }

    private fun initObserver(binding : ReviewMovieFragmentBinding){
        viewModel.reviewState.observe(viewLifecycleOwner){
            when (it){
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if(paging > it.data.totalPages!!){
                        viewModel.stopLoading()
                    }else{
                        viewModel.startLoading()
                    }
                    if(tempPagging==1){
                        if (it.data.results!!.isEmpty()){
                            binding.rvReviews.visibility = View.GONE
                            binding.ivNotFoundReview.visibility = View.VISIBLE
                            binding.tvNotFoundReview.visibility = View.VISIBLE
                        }else{
                            binding.rvReviews.visibility = View.VISIBLE
                            binding.tvNotFoundReview.visibility = View.GONE
                            binding.ivNotFoundReview.visibility = View.GONE
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
                else ->{

                }
            }
        }
    }

    private fun onScrollAdapter(binding : ReviewMovieFragmentBinding, id: String){
        binding.rvReviews.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (binding.rvReviews.layoutManager!! as LinearLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (binding.rvReviews.layoutManager!! as LinearLayoutManager).itemCount
                    if (viewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        viewModel.stopLoading()
                        viewModel.getListReview(id, paging.toString(), Const.language)
                        progressDialog.show()
                    }

                }
            }
        })
    }

}