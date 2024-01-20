package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.views.DashboardItemView
import com.kbcoding.androiddevelopmentbasics.app.utils.observeResults
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentDashboardBinding
import com.kbcoding.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    override val viewModel by viewModels<DashboardViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearBoxViews()

        binding.resultView.setTryAgainAction { viewModel.reload() }
        viewModel.boxes.observeResults(this, view, binding.resultView) {
            renderBoxes(it)
        }
    }

    private fun renderBoxes(boxes: List<Box>) {
        clearBoxViews()
        if (boxes.isEmpty()) {
            binding.noBoxesTextView.visibility = View.VISIBLE
            binding.boxesContainer.visibility = View.INVISIBLE
        } else {
            binding.noBoxesTextView.visibility = View.INVISIBLE
            binding.boxesContainer.visibility = View.VISIBLE
            createBoxes(boxes)
        }
    }

    private fun createBoxes(boxes: List<Box>) {

        // let's create boxes here by using dynamic view generation

        val width = resources.getDimensionPixelSize(R.dimen.dashboard_item_width)
        val height = resources.getDimensionPixelSize(R.dimen.dashboard_item_height)
        val generatedIdentifiers = boxes.map { box ->
            val id = View.generateViewId()
            val dashboardItemView = DashboardItemView(requireContext())
            dashboardItemView.setBox(box)
            dashboardItemView.id = id
            dashboardItemView.tag = box
            dashboardItemView.setOnClickListener(boxClickListener)
            val params = ConstraintLayout.LayoutParams(width, height)
            binding.boxesContainer.addView(dashboardItemView, params)
            return@map id
        }.toIntArray()
        binding.flowView.referencedIds = generatedIdentifiers
    }

    private fun clearBoxViews() {
        if (binding.boxesContainer.childCount > 1) {
            binding.boxesContainer.removeViews(1, binding.boxesContainer.childCount - 1)
        }
    }

    private val boxClickListener = View.OnClickListener {
        val box = it.tag as Box
        val direction = DashboardFragmentDirections.actionDashboardFragmentToBoxFragment(
            box.id,
            box.colorName,
            box.colorValue
        )
        findNavController().navigate(direction)
    }


}