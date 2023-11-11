package com.kbcoding.androiddevelopmentbasics.presentation.main.tabs.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxBinding
import com.kbcoding.androiddevelopmentbasics.di.Repositories
import com.kbcoding.androiddevelopmentbasics.presentation.views.DashboardItemView
import com.kbcoding.androiddevelopmentbasics.utils.observeEvent
import com.kbcoding.androiddevelopmentbasics.utils.viewModelCreator
import com.kbcoding.core.BaseFragment

class BoxFragment : BaseFragment<FragmentBoxBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBoxBinding {
        return FragmentBoxBinding.inflate(inflater, container, false)
    }
    private val viewModel by viewModelCreator { BoxViewModel(getBoxId(), Repositories.boxesRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setBackgroundColor(DashboardItemView.getBackgroundColor(getColorValue()))
        binding.boxTextView.text = getString(R.string.this_is_box, getColorName())

        binding.goBackButton.setOnClickListener { onGoBackButtonPressed() }

        listenShouldExitEvent()
    }

    private fun onGoBackButtonPressed() {
        TODO("Go back to the previous screen here")
    }

    private fun listenShouldExitEvent() = viewModel.shouldExitEvent.observeEvent(viewLifecycleOwner) { shouldExit ->
        if (shouldExit) {
            // close the screen if the box has been deactivated
            TODO("Go back to the previous screen here")
        }
    }

    private fun getBoxId(): Int {
        TODO("Extract box id from arguments here")
    }

    private fun getColorValue(): Int {
        TODO("Extract color value from arguments here")
    }

    private fun getColorName(): String {
        TODO("Extract color name from arguments here")
    }

}