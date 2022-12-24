package com.moyehics.news.ui

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        (requireActivity()as MainActivity).closeDrawer()
        val onBordingData: MutableList<OnBordingData> = ArrayList()
        onBordingData.add(
            OnBordingData(
                getString(R.string.page1_title),
                getString(R.string.page1_desc),
                R.drawable.onbordimage1
            )
        )
        onBordingData.add(
            OnBordingData(
                getString(R.string.page2_title),
                getString(R.string.page2_desc),
                R.drawable.onbordimage2
            )
        )
        onBordingData.add(
            OnBordingData(
                getString(R.string.page3_title),
                getString(R.string.page3_desc),
                R.drawable.onbordimage3
            )
        )
        setOnBordingViewPagerAdapter(onBordingData)
        binding.txtNext.setOnClickListener {
            position = binding.onbordingvp.currentItem
            if(binding.txtNext.text.equals(getString(R.string.next))){
                if(position<onBordingData.size){
                    position++
                    binding.onbordingvp.currentItem=position
                }
                if(position == onBordingData.size){
                    findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                }
            }


        }
        binding.txtSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
        val view = binding.root
        return view
    }

    private fun setOnBordingViewPagerAdapter(onBordingData: List<OnBordingData>) {

        onBordiingViewPagerAdapter = OnBordiingViewPagerAdapter(requireContext(), onBordingData)
        binding.onbordingvp.adapter = onBordiingViewPagerAdapter
        binding.tablayout.setupWithViewPager(binding.onbordingvp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}