package com.kbcoding.androiddevelopmentbasics.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import com.kbcoding.androiddevelopmentbasics.BoxCountItem
import com.kbcoding.androiddevelopmentbasics.Options
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.contract.CustomAction
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomAction
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomTitle
import com.kbcoding.androiddevelopmentbasics.contract.navigator
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentOptionsBinding

class OptionsFragment : BaseFragment<FragmentOptionsBinding>(), HasCustomTitle, HasCustomAction {

    private lateinit var options: Options

    private lateinit var boxCountItems: List<BoxCountItem>
    private lateinit var adapter: ArrayAdapter<BoxCountItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOptions(savedInstanceState)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOptionsBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupCheckBox()
        updateUi()
        setupListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_OPTIONS, options)
    }

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.done,
            onCustomAction = Runnable {
                onConfirmPressed()
            }
        )
    }

    override fun getTitleRes(): Int = R.string.options

    private fun setupSpinner() {
        boxCountItems =
            (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it)) }
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            boxCountItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        binding.boxCountSpinner.adapter = adapter
        binding.boxCountSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val count = boxCountItems[position].count
                    options = options.copy(boxCount = count)
                }
            }
    }

    private fun setupListeners() {
        binding.cancelButton.setOnClickListener { onCancelPressed() }
        binding.confirmButton.setOnClickListener { onConfirmPressed() }
    }

    private fun setupCheckBox() {
        binding.enableTimerCheckBox.setOnClickListener {
            options = options.copy(isTimerEnabled = binding.enableTimerCheckBox.isChecked)
        }
    }

    private fun updateUi() {
        binding.enableTimerCheckBox.isChecked = options.isTimerEnabled

        val currentIndex = boxCountItems.indexOfFirst { it.count == options.boxCount }
        binding.boxCountSpinner.setSelection(currentIndex)
    }

    private fun onCancelPressed() {
        navigator().goBack()
    }

    private fun onConfirmPressed() {
        navigator().publishResult(options)
        navigator().goBack()
    }

    private fun initOptions(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            options = savedInstanceState?.getSerializable(KEY_OPTIONS, Options::class.java)
                ?: arguments?.getSerializable(ARG_OPTIONS, Options::class.java)
                        ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
        } else {
            options = savedInstanceState?.getParcelable(KEY_OPTIONS)
                ?: arguments?.getParcelable(ARG_OPTIONS)
                        ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
        }
    }

    companion object {
        private val ARG_OPTIONS = "ARG_OPTIONS"
        private val KEY_OPTIONS = "KEY_OPTIONS"

        fun createArgs(options: Options) = bundleOf(ARG_OPTIONS to options)
    }
}