package com.moyehics.news.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moyehics.news.R
import com.moyehics.news.adapter.NewsAdapter
import com.moyehics.news.databinding.FragmentHomeBinding
import com.moyehics.news.util.*
import com.moyehics.news.util.Constants.QUERY_PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG:String="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    private var pagination = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        observer()
        setUpRecyclerView()
        //viewModel.getNews("general", Api.API_kEY)
        Log.d(TAG,viewModel.newsResponse?.articles?.size.toString())
        newsAdapter.setOnItemClicListener {
            val bundle = Bundle().apply {
                putString("article", it.url)
            }
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment, bundle)
        }

    }

    private fun setUpRecyclerView() {
        val context = requireContext()
        newsAdapter = NewsAdapter(context)
        binding.newsRecyclerView?.adapter = newsAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.newsRecyclerView?.layoutManager = layoutManager
        binding.newsRecyclerView.addOnScrollListener(this@HomeFragment.scrollListener)

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.hide()
        isLoading = false
        pagination = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.show()
        isLoading = true
    }

    private fun observer() {
        viewModel.news.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loding -> {
                    if(!pagination) {
                        binding.newsRecyclerView.gone()
                        binding.shammerNewsRecyclerView.show()
                    }
                    if (pagination) {
                        showProgressBar()
                    }

                }
                is UiState.Failure -> {
                    state.error?.let {
                        if (viewModel.newsPage>1){
                            handleApiError()
                        }else{
                            toast(it)
                        }
                    }
                }
                is UiState.Success -> {
                    newsAdapter.differ.submitList(state.data.articles.toList())
                    val totalPages = state.data.totalResults / QUERY_PAGE_SIZE +1
                    isLastPage = viewModel.newsPage.toLong() == totalPages
                    if(!pagination) {
                        binding.newsRecyclerView.show()
                        binding.shammerNewsRecyclerView.gone()
                    }
                    if(pagination) {
                        hideProgressBar()
                    }
                }
            }
        }

    }

    private fun handleApiError() {
        newsAdapter.differ.submitList(viewModel.newsResponse?.articles?.toList())
        if(!pagination) {
            binding.newsRecyclerView.show()
            binding.shammerNewsRecyclerView.gone()
        }
        if(pagination) {
            hideProgressBar()
        }
        isLastPage=true
    }


    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = (lastVisibleItemPosition+1) >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                pagination =true
                viewModel.getNews("general", Api.API_kEY)
                isScrolling = false
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}