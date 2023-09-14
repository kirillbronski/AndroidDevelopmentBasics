package com.kbcoding.androiddevelopmentbasics.core.extensions

import androidx.fragment.app.Fragment
import com.kbcoding.androiddevelopmentbasics.App
import com.kbcoding.androiddevelopmentbasics.core.factory.ViewModelFactory

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)