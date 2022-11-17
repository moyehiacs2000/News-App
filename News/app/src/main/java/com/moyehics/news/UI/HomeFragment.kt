package com.moyehics.news.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.moyehics.news.databinding.FragmentHomeBinding
import com.moyehics.news.util.UiState
import com.moyehics.news.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG:String="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val viewModel : NewsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        viewModel.getNews("tesla","b8869d637b0c4f4cae3021e6967b369f")

    }

    private fun observer() {
        viewModel.news.observe(viewLifecycleOwner){ state->
            when(state){
                is UiState.Loding ->{
                    toast("Loading...")
                }
                is UiState.Failure->{
                    state.error?.let {
                        toast(it)
                    }
                }
                is UiState.Success ->{
                    toast(state.data.articles.get(0).description)
                    Log.d(TAG,state.data.toString())
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}