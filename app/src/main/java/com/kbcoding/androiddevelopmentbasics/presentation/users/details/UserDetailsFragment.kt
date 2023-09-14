package com.kbcoding.androiddevelopmentbasics.presentation.users.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.core.presentation.BaseFragment
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserDetailsBinding.inflate(inflater, container, false)

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserDetailsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}