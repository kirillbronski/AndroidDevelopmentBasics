package com.kbcoding.androiddevelopmentbasics.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomTitle
import com.kbcoding.androiddevelopmentbasics.contract.navigator
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxBinding

class BoxFragment : BaseFragment<FragmentBoxBinding>(), HasCustomTitle {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
    }

    private fun setupListener() {
        binding.btnToMainMenuButton.setOnClickListener { onToMainMenuPressed() }
    }

    private fun onToMainMenuPressed() {
        navigator().goToMenu()
    }

    override fun getTitleRes(): Int = R.string.box

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBoxBinding.inflate(inflater, container, false)
}