package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentSettingsBinding
import com.kbcoding.androiddevelopmentbasics.di.Repositories
import com.kbcoding.androiddevelopmentbasics.utils.observeEvent
import com.kbcoding.androiddevelopmentbasics.utils.viewModelCreator
import com.kbcoding.core.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModelCreator { SettingsViewModel(Repositories.boxesRepository) }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = setupList()
        viewModel.boxSettings.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.showErrorMessageEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupList(): SettingsAdapter {
        binding.settingsList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SettingsAdapter(viewModel)
        binding.settingsList.adapter = adapter
//        val itemAnimator = binding.settingsList.itemAnimator
//        if (itemAnimator is DefaultItemAnimator) {
//            itemAnimator.supportsChangeAnimations = false
//        }
        //binding.settingsList.itemAnimator = null
        return adapter
    }

}