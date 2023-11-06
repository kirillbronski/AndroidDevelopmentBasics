package com.kbcoding.androiddevelopmentbasics.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentSecretBinding

class SecretFragment : BaseFragment<FragmentSecretBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSecretBinding {
        return FragmentSecretBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeBoxButton.setOnClickListener {
            popBackStack(destinationId = R.id.rootFragment, isInclusive = false)
        }
        binding.goBackButton.setOnClickListener {
            popBackStack()
        }
    }

    companion object {
        val TAG = SecretFragment::class.java.simpleName
    }

}