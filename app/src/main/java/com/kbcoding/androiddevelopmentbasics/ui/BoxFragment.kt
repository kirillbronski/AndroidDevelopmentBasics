package com.kbcoding.androiddevelopmentbasics.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxBinding
import kotlin.random.Random

class BoxFragment : BaseFragment<FragmentBoxBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBoxBinding {
        return FragmentBoxBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val color = requireArguments().getInt(ARG_COLOR)
        binding.root.setBackgroundColor(color)

        binding.goBackButton.setOnClickListener {
            popBackStack()
        }
        binding.openSecretButton.setOnClickListener {
            navigateTo(R.id.action_boxFragment_to_secretFragment)
        }
        binding.generateNumberButton.setOnClickListener {
            val number = Random.nextInt(until = 100)
            parentFragmentManager.setFragmentResult(
                REQUEST_CODE,
                bundleOf(EXTRA_RANDOM_NUMBER to number)
            )
            popBackStack()
        }
    }

    companion object {
        val TAG = BoxFragment::class.java.simpleName
        const val ARG_COLOR = "color"

        const val REQUEST_CODE = "RANDOM_NUMBER_REQUEST_CODE"
        const val EXTRA_RANDOM_NUMBER = "EXTRA_RANDOM_NUMBER"
    }
}