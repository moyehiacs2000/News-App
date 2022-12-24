package com.moyehics.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.moyehics.news.databinding.FragmentSavedBinding
import com.moyehics.news.util.Constants

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    lateinit var savedNewsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var  toolBar : MaterialToolbar
    private var isLogout = false
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        toolBar = (activity as MainActivity).toolBar
        setUpRecyclerView()
        viewModel.getSavedNews().observe(viewLifecycleOwner){ articles ->
            savedNewsAdapter.differ.submitList(articles)
        }
        savedNewsAdapter.setOnItemClicListener {
            val bundle = Bundle().apply {
                putString("article", it.url)
                putBoolean("from", true)
            }
            findNavController().navigate(R.id.action_savedFragment_to_articleFragment, bundle)
        }
        savedNewsAdapter.setOnSaveClickedListener { article ->
            viewModel.deleteArticle(article)
            Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.saveArticle(article)
                }
                show()
            }
        }
        savedNewsAdapter.setOnShareClickedListener {
            (activity as MainActivity).shareData(it.url)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = savedNewsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedNewsRecyclerView)
        }
        toolBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.SearchOption ->{
                    val bundle = Bundle().apply {
                        putString("from", Constants.save)
                    }
                    findNavController().navigate(R.id.action_savedFragment_to_searchFragment,bundle)
                    true
                }
                R.id.Logout ->{
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
        savedNewsAdapter = NewsAdapter(context)
        binding.savedNewsRecyclerView.adapter = savedNewsAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.savedNewsRecyclerView.layoutManager = layoutManager

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
                        findNavController().navigate(R.id.action_savedFragment_to_loginFragment)
                    }
                } else if (profile.providerId == EmailAuthProvider.PROVIDER_ID) {
                    auth.signOut()
                    findNavController().navigate(R.id.action_savedFragment_to_loginFragment)

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
                    findNavController().navigate(R.id.action_savedFragment_to_loginFragment)
                }
            }
        }
    }
}