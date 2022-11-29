package com.moyehics.news.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moyehics.news.R
import com.moyehics.news.databinding.FragmentLoginBinding
import com.moyehics.news.ui.MainActivity
import com.moyehics.news.util.isValidEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var isLogin = false
    val viewModel : AuthenticationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtDoNotHaveAcount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.txtrememberPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            isLogin = true

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
        if (binding.passwordEditeText.text.isNullOrEmpty()){
            isValid = false
            binding.passwordEditeText.error=getString(R.string.enter_password)
        }else{
            if (binding.passwordEditeText.text.toString().length<8){
                isValid = false
                binding.passwordEditeText.error=getString(R.string.digits8)

            }
        }
        return isValid
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (isLogin){
            (requireActivity()as MainActivity).openDrawer()
        }
    }

}