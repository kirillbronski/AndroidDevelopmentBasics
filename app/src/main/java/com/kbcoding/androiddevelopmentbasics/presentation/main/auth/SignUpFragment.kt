package com.kbcoding.androiddevelopmentbasics.presentation.main.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentSignUpBinding
import com.kbcoding.androiddevelopmentbasics.di.Repositories
import com.kbcoding.androiddevelopmentbasics.domain.model.accounts.SignUpData
import com.kbcoding.androiddevelopmentbasics.utils.observeEvent
import com.kbcoding.androiddevelopmentbasics.utils.viewModelCreator
import com.kbcoding.core.BaseFragment

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    private val viewModel by viewModelCreator { SignUpViewModel(Repositories.accountsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountButton.setOnClickListener { onCreateAccountButtonPressed() }

        if (savedInstanceState == null && getEmailArgument() != null) {
            binding.emailEditText.setText(getEmailArgument())
        }

        observeState()
        observeGoBackEvent()
        observeShowSuccessSignUpMessageEvent()
    }

    private fun onCreateAccountButtonPressed() {
        val signUpData = SignUpData(
            email = binding.emailEditText.text.toString(),
            username = binding.usernameEditText.text.toString(),
            password = binding.passwordEditText.text.toString(),
            repeatPassword = binding.repeatPasswordEditText.text.toString(),
        )
        viewModel.signUp(signUpData)
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        binding.createAccountButton.isEnabled = state.enableViews
        binding.emailTextInput.isEnabled = state.enableViews
        binding.usernameTextInput.isEnabled = state.enableViews
        binding.passwordTextInput.isEnabled = state.enableViews
        binding.repeatPasswordTextInput.isEnabled = state.enableViews

        fillError(binding.emailTextInput, state.emailErrorMessageRes)
        fillError(binding.usernameTextInput, state.usernameErrorMessageRes)
        fillError(binding.passwordTextInput, state.passwordErrorMessageRes)
        fillError(binding.repeatPasswordTextInput, state.repeatPasswordErrorMessageRes)

        binding.progressBar.visibility = if (state.showProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun observeShowSuccessSignUpMessageEvent() =
        viewModel.showSuccessSignUpMessageEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.sign_up_success, Toast.LENGTH_LONG).show()
        }

    private fun fillError(input: TextInputLayout, @StringRes stringRes: Int) {
        if (stringRes == SignUpViewModel.NO_ERROR_MESSAGE) {
            input.error = null
            input.isErrorEnabled = false
        } else {
            input.error = getString(stringRes)
            input.isErrorEnabled = true
        }
    }

    private fun observeGoBackEvent() = viewModel.goBackEvent.observeEvent(viewLifecycleOwner) {
        TODO("Go back to the previous screen here")
    }

    private fun getEmailArgument(): String? {
        TODO("Extract email value from arguments here")
    }
}