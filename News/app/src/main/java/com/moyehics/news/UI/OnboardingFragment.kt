package com.moyehics.news.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moyehics.news.R
import com.moyehics.news.adapter.OnBordiingViewPagerAdapter
import com.moyehics.news.databinding.FragmentOnboardingBinding
import com.moyehics.news.data.model.OnBordingData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    var onBordiingViewPagerAdapter: OnBordiingViewPagerAdapter? = null
    var position = 0
    var sharedPreferences:SharedPreferences?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val onBordingData: MutableList<OnBordingData> = ArrayList()
        onBordingData.add(
            OnBordingData(
                "Get All Types of News",
                "All Type of News From all Trusted Sources For all\nType of People",
                R.drawable.onbordimage1
            )
        )
        onBordingData.add(
            OnBordingData(
                "Get The Updated News",
                "Come on, get the latest and updated Articles Easily\nwith us every day",
                R.drawable.onbordimage2
            )
        )
        onBordingData.add(
            OnBordingData(
                "Search for News",
                "You can search for All Type of News From\nall Trusted Sources ",
                R.drawable.onbordimage3
            )
        )
        setOnBordingViewPagerAdapter(onBordingData)
        binding.txtNext.setOnClickListener {
            position = binding.onbordingvp!!.currentItem
            if(binding.txtNext.text.equals("Next")){
                if(position<onBordingData.size){
                    position++
                    binding.onbordingvp!!.currentItem=position
                }
                if(position == onBordingData.size){
                    findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                }
            }


        }
        binding.txtSkip
        val view = binding.root
        return view
    }

    private fun setOnBordingViewPagerAdapter(onBordingData: List<OnBordingData>) {

        onBordiingViewPagerAdapter = OnBordiingViewPagerAdapter(requireContext(), onBordingData)
        binding.onbordingvp!!.adapter = onBordiingViewPagerAdapter
        binding.tablayout?.setupWithViewPager(binding.onbordingvp)
    }
    private fun savePrefData(){
        sharedPreferences = requireContext().getSharedPreferences("Setting",Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("ISFirstTimerun",true)
        editor.apply()

    }
    private fun restorPrefData() : Boolean{
        sharedPreferences = requireContext().getSharedPreferences("Setting",Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("ISFirstTimerun",false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}