package com.kbcoding.androiddevelopmentbasics.presentation.currentColor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentCurrentColorBinding
import com.kbcoding.androiddevelopmentbasics.presentation.base.BaseFragment
import com.kbcoding.androiddevelopmentbasics.presentation.base.BaseScreen
import com.kbcoding.androiddevelopmentbasics.presentation.base.screenViewModel

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