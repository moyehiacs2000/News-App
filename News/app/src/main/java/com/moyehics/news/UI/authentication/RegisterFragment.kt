package com.moyehics.news.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moyehics.news.R
import com.moyehics.news.data.model.User
import com.moyehics.news.databinding.FragmentRegisterBinding
import com.moyehics.news.ui.MainActivity
import com.moyehics.news.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    val TAG:String="RegisterFragment"
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    val viewModel : AuthenticationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()as MainActivity).closeDrawer()
        observer()
        binding.registerButton.setOnClickListener {
            if(validation()){
                viewModel.regiter(
                    email = binding.emailEditeText.text.toString(),
                    password = binding.passwordEditeText.text.toString(),
                    user = getUserObj()
                )
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if(binding.userFirstNameEditeText.text.isNullOrBlank()){
            isValid=false
            binding.userFirstNameEditeText.error=getString(R.string.enter_first_name)
        }
        if(binding.userSecondNameEditeText.text.isNullOrBlank()){
            isValid=false
            binding.userSecondNameEditeText.error=getString(R.string.enter_last_name)
        }
        if(binding.emailEditeText.text.isNullOrBlank()){
            isValid=false
            binding.emailEditeText.error=getString(R.string.enter_email)
        }else{
            if(!binding.emailEditeText.text.toString().isValidEmail()){
                isValid =false
                binding.emailEditeText.error=getString(R.string.invalid_email)
            }
        }
        if(binding.passwordEditeText.text.isNullOrBlank()){
            isValid=false
            binding.passwordEditeText.error=getString(R.string.enter_password)
        }else{
            if(binding.passwordEditeText.text.toString().length<8){
                isValid=false
                binding.passwordEditeText.error=getString(R.string.digits8)
            }
        }
        if(binding.confirmPasswordEditeText.text.isNullOrBlank()){
            isValid=false
            binding.confirmPasswordEditeText.error=getString(R.string.confirm_password)
        }else{
            if(binding.confirmPasswordEditeText.text.toString().length<8){
                isValid=false
                binding.confirmPasswordEditeText.error=getString(R.string.digits8)
            }
        }
        if(!binding.passwordEditeText.text.toString().equals(binding.confirmPasswordEditeText.text.toString())){
            isValid=false
            toast(getString(R.string.passwords_identical))
        }
        return isValid
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loding ->{
                    binding.registerButton.hide()
                    binding.progressBar.show()

                }
                is UiState.Failure ->{
                    binding.registerButton.show()
                    binding.progressBar.hide()
                    state.error?.let { toast(it) }
                }
                is UiState.Success -> {
                    binding.registerButton.show()
                    binding.progressBar.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }
    fun getUserObj():User{
        return User(
            id="",
            firstName = binding.userFirstNameEditeText.text.toString(),
            lastName = binding.userSecondNameEditeText.text.toString(),
            email = binding.emailEditeText.text.toString()
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}