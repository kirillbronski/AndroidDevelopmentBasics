package com.kbcoding.androiddevelopmentbasics.presentation.users.details

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentUserDetailsBinding
import com.kbcoding.androiddevelopmentbasics.presentation.users.list.UsersListViewModel

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    private val viewModel: UserDetailsViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(ARG_USER_ID)
        viewModel.loadUser(id!!)

        setupListener()
        setupObservers()
    }

    private fun setupListener() {
        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
            navigator().toast(R.string.user_has_been_deleted)
            navigator().goBack()
        }
    }

    private fun setupObservers() {
        viewModel.userDetails.observe(viewLifecycleOwner) {
            binding.userNameTextView.text = it.user.name
            setupPhoto(it.user.photo)
            binding.userDetailsTextView.text = it.details
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