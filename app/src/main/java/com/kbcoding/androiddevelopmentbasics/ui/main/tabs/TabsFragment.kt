package com.kbcoding.androiddevelopmentbasics.ui.main.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentTabsBinding
import com.kbcoding.androiddevelopmentbasics.di.Repositories
import com.kbcoding.androiddevelopmentbasics.utils.viewModelCreator
import com.kbcoding.core.BaseFragment

class TabsFragment : BaseFragment<FragmentTabsBinding>() {

    private val viewModel by viewModelCreator { TabsViewModel(Repositories.accountsRepository) }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTabsBinding {
        return FragmentTabsBinding.inflate(inflater, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        observeAdminTab()
    }

    private fun observeAdminTab() {
        viewModel.showAdminTab.observe(viewLifecycleOwner) { showAdminTab ->
            binding.bottomNavigationView.menu.findItem(R.id.admin).isVisible = showAdminTab
        }
    }

}