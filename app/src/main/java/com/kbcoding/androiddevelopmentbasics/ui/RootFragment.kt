package com.kbcoding.androiddevelopmentbasics.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentRootBinding
import com.kbcoding.androiddevelopmentbasics.ui.BoxFragment.Companion.EXTRA_RANDOM_NUMBER

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

        val liveData = findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(EXTRA_RANDOM_NUMBER)
        liveData?.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), "Generated number: $it", Toast.LENGTH_SHORT).show()
            }
            liveData.value = null
        }
    }

    private fun openBox(color: Int, colorName: String) {
        navigateTo(RootFragmentDirections.actionRootFragmentToBoxFragment(colorName, color))
    }
}