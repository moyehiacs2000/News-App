package com.moyehics.news.ui.authentication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.moyehics.news.R
import com.moyehics.news.databinding.FragmentLoginBinding
import com.moyehics.news.ui.MainActivity
import com.moyehics.news.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var isLogin = false
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    var callbackManager = CallbackManager.Factory.create()
    private val TAG = "LoginFragment"
    val viewModel: AuthenticationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).closeDrawer()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

            if (validation()) {
                observer()
                login()
            }
        }
        binding.googleLogo.setOnClickListener {
            binding.googleLogo.hide()
            binding.googleProgressBar.show()
            googleObserver()
            signInWithGoogle()
        }
        binding.facebookLogo.setOnClickListener {
            facebookLogin()
            facebookObserver()
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                callbackManager,
                listOf("public_profile", "email")
            )
        }
    }

    private fun login() {
        viewModel.login(
            email = binding.emailEditeText.text.toString(),
            password = binding.passwordEditeText.text.toString()
        )
    }

    fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loding -> {
                    binding.loginButton.hide()
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.loginButton.show()
                    binding.progressBar.hide()
                    state.error?.let { toast(it) }
                }
                is UiState.Success -> {
                    binding.loginButton.show()
                    binding.progressBar.hide()
                    toast(state.data)
                    isLogin = true
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                else -> {
                    toast("error")
                }
            }

        }
    }
    private fun facebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                viewModel.facebookLogin(loginResult.accessToken)
            }
            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                toast("Login Failed")
            }
        })

    }

    fun facebookObserver(){
        viewModel.facebookLogin.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loding -> {
                    binding.facebookLogo.hide()
                    binding.facebookProgressBar.show()
                }
                is UiState.Failure ->{
                    binding.facebookLogo.show()
                    binding.facebookProgressBar.hide()
                    state.error?.let { toast(it) }
                }
                is UiState.Success ->{
                    isLogin = true
                    binding.facebookLogo.show()
                    binding.facebookProgressBar.hide()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }

        }
    }
    private fun signInWithGoogle() {
        createRequest()
        val signInIntent = mGoogleSignInClient.signInIntent
        launchSomeActivity.launch(signInIntent)
    }
    private fun createRequest() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }
    fun googleObserver(){
        viewModel.googleLogin.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loding -> {

                }
                is UiState.Failure ->{
                    binding.googleLogo.show()
                    binding.googleProgressBar.hide()
                    state.error?.let { toast(it) }
                }
                is UiState.Success ->{
                    binding.googleLogo.show()
                    binding.googleProgressBar.hide()
                    isLogin = true
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }

        }
    }
    fun validation(): Boolean {
        var isValid = true
        if (binding.emailEditeText.text.toString().isNullOrEmpty()) {
            isValid = false
            binding.emailEditeText.error = getString(R.string.enter_email)
        } else {
            if (!binding.emailEditeText.text.toString().isValidEmail()) {
                isValid = false
                binding.emailEditeText.error = getString(R.string.invalid_email)
            }
        }
        if (binding.passwordEditeText.text.isNullOrEmpty()) {
            isValid = false
            binding.passwordEditeText.error = getString(R.string.enter_password)
        } else {
            if (binding.passwordEditeText.text.toString().length < 8) {
                isValid = false
                binding.passwordEditeText.error = getString(R.string.digits8)

            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (isLogin) {
            (requireActivity() as MainActivity).openDrawer()
        }
    }

    var launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                if (data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!

                        viewModel.googleLogin(account)

                    } catch (e: ApiException) {
                        toast("Login Failed")
                        binding.googleLogo.show()
                        binding.googleProgressBar.hide()
                    }
                }
            } else {
                binding.googleLogo.show()
                binding.googleProgressBar.hide()
            }
        }


}