package com.kbcoding.androiddevelopmentbasics.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.Options
import com.kbcoding.androiddevelopmentbasics.contract.navigator
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentMenuBinding

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    private lateinit var options: Options

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMenuBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            options = savedInstanceState?.getParcelable(KEY_OPTIONS, Options::class.java) ?: Options.DEFAULT
        } else {
            options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenResultByNavigator()
        initializeListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_OPTIONS, options)
    }

    private fun listenResultByNavigator() {
        navigator().listenResult<Options>(viewLifecycleOwner) {
            this.options = it
        }
    }

    private fun initializeListeners() {
        with(binding) {
            btnOpenBox.setOnClickListener { onOpenBoxPressed() }
            btnOptions.setOnClickListener { onOptionsPressed() }
            btnAbout.setOnClickListener { onAboutPressed() }
            btnExit.setOnClickListener { onExitPressed() }
        }
    }

    private fun onOpenBoxPressed() {
        navigator().showBoxSelectionScreen(options = options)
    }

    private fun onOptionsPressed() {
        navigator().showOptionsScreen(options = options)
    }

    private fun onAboutPressed() {
        navigator().showAboutScreen()
    }

    private fun onExitPressed() {
        navigator().goBack()
    }

    companion object {
        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"
    }
}