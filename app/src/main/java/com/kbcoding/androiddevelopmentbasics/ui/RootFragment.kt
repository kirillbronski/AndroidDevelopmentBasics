package com.kbcoding.androiddevelopmentbasics.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentRootBinding
import com.kbcoding.androiddevelopmentbasics.ui.BoxFragment.Companion.ARG_COLOR
import com.kbcoding.androiddevelopmentbasics.ui.BoxFragment.Companion.ARG_COLOR_NAME
import com.kbcoding.androiddevelopmentbasics.ui.BoxFragment.Companion.EXTRA_RANDOM_NUMBER
import com.kbcoding.androiddevelopmentbasics.ui.BoxFragment.Companion.REQUEST_CODE

class RootFragment : BaseFragment<FragmentRootBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRootBinding {
        return FragmentRootBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenGreenBox.setOnClickListener {
            openBox(Color.rgb(200, 255, 200), "Green")
        }

        binding.btnOpenYellowBox.setOnClickListener {
            openBox(Color.rgb(255, 255, 200), "Yellow")
        }

        parentFragmentManager.setFragmentResultListener(REQUEST_CODE, viewLifecycleOwner){_, data ->
            val number = data.getInt(EXTRA_RANDOM_NUMBER)
            Toast.makeText(requireContext(), "Generated number: $number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openBox(color: Int, colorName: String) {
        navigateTo(
            R.id.action_rootFragment_to_boxFragment,
            bundleOf(ARG_COLOR to color, ARG_COLOR_NAME to colorName))
    }


}