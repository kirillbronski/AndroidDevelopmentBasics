package com.kbcoding.androiddevelopmentbasics.presentation.currentColor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentCurrentColorBinding
import com.kbcoding.androiddevelopmentbasics.databinding.PartResultBinding
import com.kbcoding.androiddevelopmentbasics.presentation.onTryAgain
import com.kbcoding.androiddevelopmentbasics.presentation.renderSimpleResult
import com.kbcoding.core.presentation.BaseFragment
import com.kbcoding.core.presentation.BaseScreen
import com.kbcoding.core.presentation.screenViewModel

class CurrentColorFragment : BaseFragment<FragmentCurrentColorBinding>() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCurrentColorBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }
    }

    private fun setupListeners() {
        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }
    }

}