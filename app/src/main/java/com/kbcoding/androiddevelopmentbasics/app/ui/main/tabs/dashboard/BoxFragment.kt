package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.ui.views.DashboardItemView
import com.kbcoding.androiddevelopmentbasics.app.utils.observeEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.viewModelCreator
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxBinding
import com.kbcoding.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoxFragment : BaseFragment<FragmentBoxBinding>() {

    private val args by navArgs<BoxFragmentArgs>()

    @Inject
    lateinit var factory: BoxViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(args.boxId)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBoxBinding {
        return FragmentBoxBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setBackgroundColor(DashboardItemView.getBackgroundColor(getColorValue()))
        binding.boxTextView.text = getString(R.string.this_is_box, getColorName())

        binding.goBackButton.setOnClickListener { onGoBackButtonPressed() }

        listenShouldExitEvent()
    }

    private fun onGoBackButtonPressed() {
        findNavController().popBackStack()
    }

    private fun listenShouldExitEvent() = viewModel.shouldExitEvent.observeEvent(viewLifecycleOwner) { shouldExit ->
        if (shouldExit) {
            // close the screen if the box has been deactivated
            findNavController().popBackStack()
        }
    }

    private fun getBoxId(): Long = args.boxId

    private fun getColorValue(): Int = args.colorValue

    private fun getColorName(): String = args.colorName

}