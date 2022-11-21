package com.moyehics.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moyehics.news.R
import com.moyehics.news.adapter.NewsAdapter
import com.moyehics.news.data.model.news.Article
import com.moyehics.news.databinding.FragmentHomeBinding
import com.moyehics.news.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    //val TAG:String="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : NewsViewModel by viewModels()
    lateinit var newsAdapter:NewsAdapter
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
        observer()
        setUpRecyclerView()
        viewModel.getNews("general", Api.API_kEY)
        newsAdapter.setOnItemClicListener {
            val bundle = Bundle().apply {
                putString("article",it.url)
            }
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment,bundle)
        }

    }
    private fun setUpRecyclerView() {
        val context = requireContext()
        newsAdapter = NewsAdapter(context)
        binding.cityRecyclerView?.adapter=newsAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.cityRecyclerView?.layoutManager=layoutManager

    }
    private fun observer() {
        viewModel.news.observe(viewLifecycleOwner){ state->
            when(state){
                is UiState.Loding ->{
                    binding.cityRecyclerView.gone()
                    binding.shammerCityRecyclerView.show()
                }
                is UiState.Failure->{
                    state.error?.let {
                        toast(it)
                    }
                }
                is UiState.Success ->{
                   newsAdapter.differ.submitList(state.data.articles)
                    binding.cityRecyclerView.show()
                    binding.shammerCityRecyclerView.gone()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}