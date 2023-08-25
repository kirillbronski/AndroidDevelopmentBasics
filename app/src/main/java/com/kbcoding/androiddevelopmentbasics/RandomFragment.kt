package com.kbcoding.androiddevelopmentbasics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf

class RandomFragment : Fragment() {


    companion object {

        private const val ARG_UUID = "ARG_UUID"

        private const val KEY_BACKGROUND_COLOR = "KEY_BACKGROUND_COLOR"
        private const val KEY_CHUCK_NORRIS_FACT = "KEY_CHUCK_NORRIS_FACT"

        @JvmStatic private val TAG = RandomFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(uuid: String) =
            RandomFragment().apply {
                arguments = bundleOf(ARG_UUID to uuid)
            }
    }
}