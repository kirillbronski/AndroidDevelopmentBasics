package com.kbcoding.androiddevelopmentbasics.screens.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.core.base.BaseFragment
import com.kbcoding.androiddevelopmentbasics.core.base.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.core.navigator.BaseScreen
import com.kbcoding.androiddevelopmentbasics.core.screenViewModel
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentEditBinding

class EditFragment : BaseFragment<FragmentEditBinding>() {

    class Screen(val initialValue: String) : BaseScreen

    override val viewModel by screenViewModel<EditViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialMessageEvent.observe(viewLifecycleOwner) {
            it.getValue()?.let { message -> binding.valueEditText.setText(message) }
        }

        binding.saveButton.setOnClickListener {
            viewModel.onSavePressed(binding.valueEditText.text.toString())
        }

        binding.cancelButton.setOnClickListener {
            viewModel.onCancelPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}