package com.kbcoding.androiddevelopmentbasics.screens.hello

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kbcoding.androiddevelopmentbasics.core.base.BaseFragment
import com.kbcoding.androiddevelopmentbasics.core.base.BaseViewModel
import com.kbcoding.androiddevelopmentbasics.core.navigator.BaseScreen
import com.kbcoding.androiddevelopmentbasics.core.screenViewModel
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentHelloBinding

class HelloFragment : BaseFragment<FragmentHelloBinding>() {
    class Screen : BaseScreen

    override val viewModel by screenViewModel<HelloViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHelloBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editButton.setOnClickListener {
            viewModel.onEditPressed()
        }

        viewModel.currentMessageLiveData.observe(viewLifecycleOwner){
            binding.valueTextView.text = it
        }
    }
}