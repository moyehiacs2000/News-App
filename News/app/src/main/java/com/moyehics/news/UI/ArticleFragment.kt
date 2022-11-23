package com.moyehics.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.moyehics.news.R
import com.moyehics.news.databinding.FragmentArticleBinding
import com.moyehics.news.databinding.FragmentOnboardingBinding


class ArticleFragment : Fragment() {
    private var _binding:FragmentArticleBinding? = null
    private val binding get() = _binding!!
    val args : ArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()as MainActivity).closeDrawer()
        val article = args.article
        binding.webView.apply { 
            webViewClient = WebViewClient()
            loadUrl(article)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity()as MainActivity).openDrawer()
    }

}