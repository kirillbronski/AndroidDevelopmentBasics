package com.kbcoding.androiddevelopmentbasics.presentation.users.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.core.extensions.factory
import com.kbcoding.androiddevelopmentbasics.core.extensions.navigator
import com.kbcoding.androiddevelopmentbasics.core.presentation.BaseFragment
import com.kbcoding.androiddevelopmentbasics.core.result.Result
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    private val viewModel: UserDetailsViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()
    }

    private fun setupListener() {
        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            binding.contentContainer.visibility = if (it.showContent) {
                val userDetails = (it.userDetailsResult as Result.Success).data
                binding.userNameTextView.text = userDetails.user.name
                setupPhoto(userDetails.user.photo)
                binding.userDetailsTextView.text = userDetails.details
                View.VISIBLE
            } else {
                View.GONE
        }
            binding.progressBar.visibility = if (it.showProgress) View.VISIBLE else View.GONE
            binding.deleteButton.isEnabled = it.enableDeleteButton
        }
        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageRes -> navigator().toast(messageRes) }
        }
        viewModel.actionGoBack.observe(viewLifecycleOwner) {
            it.getValue()?.let { navigator().goBack() }
        }
    }

    private fun setupPhoto(photo: String) {
        if (photo.isNotBlank()) {
            Glide.with(requireContext())
                .load(photo)
                .circleCrop()
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .into(binding.photoImageView)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.baseline_account_circle_24)
                .circleCrop()
                .into(binding.photoImageView)
        }
    }

    companion object {

        private const val ARG_USER_ID = "ARG_USER_ID"

        @JvmStatic
        fun newInstance(userId: Long) =
            UserDetailsFragment().apply {
                arguments = bundleOf(ARG_USER_ID to userId)
            }
    }
}