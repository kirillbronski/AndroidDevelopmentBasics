package com.kbcoding.androiddevelopmentbasics.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxBinding

class BoxFragment : BaseFragment<FragmentBoxBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBoxBinding {
        return FragmentBoxBinding.inflate(inflater, container, false)
    }
}