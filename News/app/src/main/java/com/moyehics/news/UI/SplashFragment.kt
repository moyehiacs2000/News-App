package com.moyehics.news.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.moyehics.news.R
import com.moyehics.news.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding:FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private var isLogin = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        (requireActivity()as MainActivity).closeDrawer()
        binding.newslogo.animate().alpha(1.0f).setDuration(3000).start()
        Handler(Looper.myLooper()!!).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if(user != null){
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                isLogin=true
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }
        },3000)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (isLogin){
            (requireActivity()as MainActivity).openDrawer()
        }
    }

}