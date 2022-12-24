package com.moyehics.news.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moyehics.news.R
import com.moyehics.news.databinding.FragmentForgotPasswordBinding
import com.moyehics.news.ui.MainActivity
import com.moyehics.news.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    val viewModel : AuthenticationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()as MainActivity).closeDrawer()
        observer()
        binding.submitButton.setOnClickListener {
            if(validation()){
                viewModel.forgotPassword(binding.emailEditeText.text.toString())
            }
        }
    }
    fun observer(){
        viewModel.forgotPassword.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loding ->{
                    binding.submitButton.hide()
                    binding.progressBar.show()
                }
                is UiState.Failure ->{
                    state.error?.let { toast(it) }
                }
                is UiState.Success -> {
                    toast(state.data)
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                }
                else -> {
                    toast("error")
                }
            }

        }
    }
    fun validation():Boolean{
        var isValid = true
        if(binding.emailEditeText.text.toString().isNullOrEmpty()){
            isValid=false
            binding.emailEditeText.error=getString(R.string.enter_email)
        }else{
            if(!binding.emailEditeText.text.toString().isValidEmail()){
                isValid = false
                binding.emailEditeText.error=getString(R.string.invalid_email)
            }
        }
        return isValid
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}