package com.kbcoding.androiddevelopmentbasics.presentation.currentColor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentCurrentColorBinding
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

        viewModel.currentColor.observe(viewLifecycleOwner) {
            binding.colorView.setBackgroundColor(it.value)
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }
    }

}