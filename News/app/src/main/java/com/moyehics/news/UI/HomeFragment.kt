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
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.moyehics.news.R
import com.moyehics.news.adapter.NewsAdapter
import com.moyehics.news.data.model.news.Article
import com.moyehics.news.databinding.FragmentHomeBinding
import com.moyehics.news.util.*
import com.moyehics.news.util.Constants.QUERY_PAGE_SIZE
import com.moyehics.news.util.Constants.home
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG: String = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var toolBar: MaterialToolbar
    private var pagination = false
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var isLogout = false
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
        toolBar = (activity as MainActivity).toolBar
        observer()
        setUpRecyclerView()

        Log.d(TAG, viewModel.newsResponse?.articles?.size.toString())
        newsAdapter.setOnItemClicListener {
            val bundle = Bundle().apply {
                putString("article", it.url)
                putBoolean("from", true)
            }
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment, bundle)
        }
        newsAdapter.setOnSaveClickedListener {
            if (!(it.isSeved)) {
                viewModel.saveArticle(it)
                Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.deleteArticle(it)
                Snackbar.make(view, "Article unsaved", Snackbar.LENGTH_SHORT).show()
            }
        }
        newsAdapter.setOnShareClickedListener {
            (activity as MainActivity).shareData(it.url)
        }
        toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.SearchOption -> {
                    val bundle = Bundle().apply {
                        putString("from", home)
                    }
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment, bundle)
                    true
                }
                R.id.Logout -> {
                    if(!isLogout){
                        logout()
                        isLogout = true
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        val context = requireContext()
        newsAdapter = NewsAdapter(context)
        binding.newsRecyclerView.adapter = newsAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.newsRecyclerView.layoutManager = layoutManager
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
                    if (!pagination) {
                        binding.newsRecyclerView.gone()
                        binding.shammerNewsRecyclerView.show()
                    }
                    if (pagination) {
                        showProgressBar()
                    }

                }
                is UiState.Failure -> {
                    state.error?.let {
                        if (viewModel.newsPage > 1 && it != "No internet connection") {
                            handleApiError()
                        }
                        toast(it)
                        if (!pagination) {
                            binding.shammerNewsRecyclerView.gone()
                        }
                        if (it == "No internet connection") {
                            hideProgressBar()
                        }
                    }
                }
                is UiState.Success -> {
                    newsAdapter.differ.submitList(state.data.articles.toList())
                    val totalPages = state.data.totalResults / QUERY_PAGE_SIZE + 1
                    isLastPage = viewModel.newsPage.toLong() == totalPages
                    if (!pagination) {
                        binding.newsRecyclerView.show()
                        binding.shammerNewsRecyclerView.gone()
                    }
                    if (pagination) {
                        hideProgressBar()
                    }
                }
            }
        }
    }

    private fun handleApiError() {
        newsAdapter.differ.submitList(viewModel.newsResponse?.articles?.toList())
        if (!pagination) {
            binding.newsRecyclerView.show()
            binding.shammerNewsRecyclerView.gone()
        }
        if (pagination) {
            hideProgressBar()
        }
        isLastPage = true
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
            val isAtLastItem = (lastVisibleItemPosition + 1) >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                pagination = true
                viewModel.getNews("general")
                isScrolling = false
            }

        }
    }

    private fun logout() {
        var auth = FirebaseAuth.getInstance()
        auth.currentUser.let {
            for (profile in it!!.providerData) {
                if (profile.providerId == GoogleAuthProvider.PROVIDER_ID) {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
                    mGoogleSignInClient.signOut().addOnCompleteListener {
                        auth.signOut()
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                } else if (profile.providerId == EmailAuthProvider.PROVIDER_ID) {
                    auth.signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

                }else if(profile.providerId == FacebookAuthProvider.PROVIDER_ID){
                    var accessToken = AccessToken.getCurrentAccessToken()
                    val request = GraphRequest.newGraphPathRequest(accessToken,
                    "/me/permissions",
                    GraphRequest.Callback {
                        auth.signOut()
                        LoginManager.getInstance().logOut()

                    })
                    request.httpMethod = HttpMethod.DELETE
                    request.executeAsync()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}