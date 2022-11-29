package com.moyehics.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.moyehics.news.adapter.NewsAdapter
import com.moyehics.news.databinding.FragmentHomeBinding
import com.moyehics.news.databinding.FragmentSavedBinding
import com.moyehics.news.ui.MainActivity
import com.moyehics.news.ui.NewsViewModel
import com.moyehics.news.util.UiState
import com.moyehics.news.util.toast

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    lateinit var savedNewsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
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
    }
    private fun setUpRecyclerView() {
        val context = requireContext()
        savedNewsAdapter = NewsAdapter(context)
        binding.savedNewsRecyclerView?.adapter = savedNewsAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.savedNewsRecyclerView?.layoutManager = layoutManager

    }
}