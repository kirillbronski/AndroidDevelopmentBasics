package com.kbcoding.androiddevelopmentbasics.presentation.main.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentSignInBinding
import com.kbcoding.androiddevelopmentbasics.di.Repositories
import com.kbcoding.androiddevelopmentbasics.utils.observeEvent
import com.kbcoding.androiddevelopmentbasics.utils.viewModelCreator
import com.kbcoding.core.BaseFragment

class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignInBinding {
        return FragmentSignInBinding.inflate(inflater, container, false)
    }

    private val viewModel by viewModelCreator { SignInViewModel(Repositories.accountsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener { onSignInButtonPressed() }
        binding.signUpButton.setOnClickListener { onSignUpButtonPressed() }

        observeState()
        observeClearPasswordEvent()
        observeShowAuthErrorMessageEvent()
        observeNavigateToTabsEvent()
    }

    private fun onSignInButtonPressed() {
        viewModel.signIn(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
        binding.emailTextInput.error =
            if (it.emptyEmailError) getString(R.string.field_is_empty) else null
        binding.passwordTextInput.error =
            if (it.emptyPasswordError) getString(R.string.field_is_empty) else null

        binding.emailTextInput.isEnabled = it.enableViews
        binding.passwordTextInput.isEnabled = it.enableViews
        binding.signInButton.isEnabled = it.enableViews
        binding.signUpButton.isEnabled = it.enableViews
        binding.progressBar.visibility = if (it.showProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun observeShowAuthErrorMessageEvent() =
        viewModel.showAuthToastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.invalid_email_or_password, Toast.LENGTH_SHORT)
                .show()
        }

    private fun observeClearPasswordEvent() =
        viewModel.clearPasswordEvent.observeEvent(viewLifecycleOwner) {
            binding.passwordEditText.text?.clear()
        }

    private fun observeNavigateToTabsEvent() =
        viewModel.navigateToTabsEvent.observeEvent(viewLifecycleOwner) {
            // user has signed in successfully
            TODO("Replace SignInFragment by TabsFragment here")
        }

    private fun onSignUpButtonPressed() {
        val email = binding.emailEditText.text.toString()
        val emailArg = if (email.isBlank())
            null
        else {
            email
        }

        // user want to create a new account
        TODO("Launch SignUpFragment here and send emailArg to it")
    }


}