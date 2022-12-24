package com.moyehics.news.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.moyehics.news.R
import com.moyehics.news.adapter.NewsAdapter
import com.moyehics.news.data.model.news.News
import com.moyehics.news.databinding.FragmentSearchBinding
import com.moyehics.news.util.*
import com.moyehics.news.util.Constants.home
import com.moyehics.news.util.Constants.save


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val TAG ="SearchFragment"
    lateinit var searchNewsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    private var pagination = false
    private var toArticle=false
    val args : SearchFragmentArgs by navArgs()
    private lateinit var from:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        (requireActivity()as MainActivity).closeDrawer()
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        from = args.from
        binding.imageback.setOnClickListener {
          back()
        }
        binding.inputSearch.requestFocus()
        binding.inputSearch.setOnEditorActionListener { _, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchText(binding.inputSearch.text.toString())
                binding.inputSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
                true
            } else {
                false
            }
        }
        viewModel = (activity as MainActivity).viewModel
        observer()
        setUpRecyclerView()
        searchNewsAdapter.setOnItemClicListener {
            val bundle = Bundle().apply {
                putString("article", it.url)
                putBoolean("from",false)
            }
            toArticle=true
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment, bundle)
        }
        searchNewsAdapter.setOnSaveClickedListener {
            if (!(it.isSeved)) {
                viewModel.saveArticle(it)
                Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.deleteArticle(it)
                Snackbar.make(view, "Article unsaved", Snackbar.LENGTH_SHORT).show()
            }
        }
        searchNewsAdapter.setOnShareClickedListener {
            (activity as MainActivity).shareData(it.url)
        }
    }

    private fun searchText(query: String) {
        if(query.isNotEmpty()){
            viewModel.searchNewsResponse=null
            viewModel.searchNewsPage = 1
            viewModel.searchForNews(query)
        }
    }
    private fun setUpRecyclerView() {
        val context = requireContext()
        searchNewsAdapter = NewsAdapter(context)
        binding.searchRecyclerView.adapter = searchNewsAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.searchRecyclerView.layoutManager = layoutManager
        binding.searchRecyclerView.addOnScrollListener(this@SearchFragment.scrollListener)
    }
    private fun observer() {
        viewModel.searchNews.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loding -> {
                    if(!pagination) {
                        binding.searchRecyclerView.gone()
                        binding.shammerNewsRecyclerView.show()
                    }
                    if (pagination) {
                        showProgressBar()
                    }

                }
                is UiState.Failure -> {
                    state.error?.let {
                        if (viewModel.searchNewsPage>1){
                            handleApiError()
                        }
                        toast(it)
                        if (!pagination) {
                            binding.shammerNewsRecyclerView.gone()
                        }
                        if(it=="No internet connection"){
                            hideProgressBar()
                        }
                    }
                }
                is UiState.Success -> {
                    searchNewsAdapter.differ.submitList(state.data.articles.toList())
                    val totalPages = state.data.totalResults / Constants.QUERY_PAGE_SIZE + 2
                    isLastPage = viewModel.searchNewsPage.toLong() == totalPages
                    if(!pagination) {
                        binding.searchRecyclerView.show()
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
        searchNewsAdapter.differ.submitList(viewModel.searchNewsResponse?.articles?.toList())
        if(!pagination) {
            binding.searchRecyclerView.show()
            binding.shammerNewsRecyclerView.gone()
        }
        if(pagination) {
            hideProgressBar()
        }
        isLastPage=true
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                pagination =true
                viewModel.searchForNews(binding.inputSearch.text.toString())
                isScrolling = false
            }

        }
    }
    fun back(){
        when(from){
            home ->{
                findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            }
            save ->{
                findNavController().navigate(R.id.action_searchFragment_to_savedFragment)
            }
        }
    }
    override fun onDestroyView() {
        if(!toArticle){
            viewModel.searchNewsResponse=null
            viewModel.searchNewsPage = 1
            viewModel._searchNews= MutableLiveData<UiState<News>>()
            (requireActivity()as MainActivity).openDrawer()
        }

        super.onDestroyView()
        _binding = null
        toArticle=false

    }

}