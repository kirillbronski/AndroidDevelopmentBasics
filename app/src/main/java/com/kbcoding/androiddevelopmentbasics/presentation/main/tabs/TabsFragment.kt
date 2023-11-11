package com.kbcoding.androiddevelopmentbasics.presentation.main.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentTabsBinding
import com.kbcoding.core.BaseFragment

class TabsFragment : BaseFragment<FragmentTabsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTabsBinding {
        return FragmentTabsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TODO("Connect Nav Component to the BottomNavigationView here")
    }

}