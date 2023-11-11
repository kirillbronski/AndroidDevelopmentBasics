package com.kbcoding.androiddevelopmentbasics.presentation.main.tabs.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentEditProfileBinding
import com.kbcoding.androiddevelopmentbasics.di.Repositories
import com.kbcoding.androiddevelopmentbasics.utils.observeEvent
import com.kbcoding.androiddevelopmentbasics.utils.viewModelCreator
import com.kbcoding.core.BaseFragment

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel by viewModelCreator { EditProfileViewModel(Repositories.accountsRepository) }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditProfileBinding {
        return FragmentEditProfileBinding.inflate(inflater, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener { onSaveButtonPressed() }
        binding.cancelButton.setOnClickListener { onCancelButtonPressed() }

        if (savedInstanceState == null) listenInitialUsernameEvent()
        observeGoBackEvent()
        observeSaveInProgress()
        observeEmptyFieldErrorEvent()
    }

    private fun onSaveButtonPressed() {
        viewModel.saveUsername(binding.usernameEditText.text.toString())
    }

    private fun observeSaveInProgress() = viewModel.saveInProgress.observe(viewLifecycleOwner) {
        if (it) {
            binding.progressBar.visibility = View.VISIBLE
            binding.saveButton.isEnabled = false
            binding.usernameTextInput.isEnabled = false
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.saveButton.isEnabled = true
            binding.usernameTextInput.isEnabled = true
        }
    }

    private fun listenInitialUsernameEvent() = viewModel.initialUsernameEvent.observeEvent(viewLifecycleOwner) { username ->
        binding.usernameEditText.setText(username)
    }

    private fun observeEmptyFieldErrorEvent() = viewModel.showEmptyFieldErrorEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), R.string.field_is_empty, Toast.LENGTH_SHORT).show()
    }

    private fun onCancelButtonPressed() {
        TODO("Go back to the previous screen here")
    }

    private fun observeGoBackEvent() = viewModel.goBackEvent.observeEvent(viewLifecycleOwner) {
        TODO("Go back to the previous screen here")
    }
}