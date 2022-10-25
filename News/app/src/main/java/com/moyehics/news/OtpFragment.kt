package com.moyehics.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moyehics.news.databinding.FragmentForgotPasswordBinding
import com.moyehics.news.databinding.FragmentLoginBinding
import com.moyehics.news.databinding.FragmentOtpBinding

class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOtpBinding.inflate(inflater,container,false)
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_resetPasswordFragment)
        }
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}