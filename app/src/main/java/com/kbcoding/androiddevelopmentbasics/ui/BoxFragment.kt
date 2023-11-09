package com.kbcoding.androiddevelopmentbasics.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxBinding
import kotlin.random.Random

class BoxFragment : BaseFragment<FragmentBoxBinding>() {

    private val args: BoxFragmentArgs by navArgs()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBoxBinding {
        return FragmentBoxBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val color = args.color
        binding.root.setBackgroundColor(color)

        binding.goBackButton.setOnClickListener {
            popBackStack()
        }
        binding.openSecretButton.setOnClickListener {
            navigateTo(BoxFragmentDirections.actionBoxFragmentToSecretFragment())
        }
        binding.generateNumberButton.setOnClickListener {
            val number = Random.nextInt(until = 100)
            findNavController().previousBackStackEntry?.savedStateHandle?.set(EXTRA_RANDOM_NUMBER, number)
            popBackStack()
        }
    }

    companion object {
        val TAG = BoxFragment::class.java.simpleName

        const val EXTRA_RANDOM_NUMBER = "EXTRA_RANDOM_NUMBER"
    }
}