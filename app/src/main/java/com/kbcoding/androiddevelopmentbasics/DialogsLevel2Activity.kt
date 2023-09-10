package com.kbcoding.androiddevelopmentbasics

import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityLevel2Binding
import com.kbcoding.androiddevelopmentbasics.databinding.PartVolumeBinding
import com.kbcoding.androiddevelopmentbasics.databinding.PartVolumeInputBinding
import com.kbcoding.androiddevelopmentbasics.entities.AvailableVolumeValues
import com.kbcoding.androiddevelopmentbasics.level2.CustomDialogFragment
import com.kbcoding.androiddevelopmentbasics.level2.CustomInputDialogFragment
import com.kbcoding.androiddevelopmentbasics.level2.CustomInputDialogListener
import com.kbcoding.androiddevelopmentbasics.level2.CustomSingleChoiceDialogFragment
import kotlin.properties.Delegates

class DialogsLevel2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLevel2Binding
    private var firstVolume by Delegates.notNull<Int>()
    private var secondVolume by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel2Binding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.showCustomAlertDialogButton.setOnClickListener {
            showCustomDialogFragment()
        }
        binding.showCustomSingleChoiceAlertDialogButton.setOnClickListener {
            showCustomSingleChoiceAlertDialog()
        }
        binding.showInputAlertDialogButton.setOnClickListener {
            showCustomInputDialogFragment(KEY_FIRST_REQUEST_KEY, firstVolume)
        }
        binding.showInputAlertDialog2Button.setOnClickListener {
            showCustomInputDialogFragment(KEY_SECOND_REQUEST_KEY, secondVolume)
        }

        firstVolume = savedInstanceState?.getInt(KEY_VOLUME_FIRST) ?: 50
        secondVolume = savedInstanceState?.getInt(KEY_VOLUME_SECOND) ?: 50
        updateUi()

        setupCustomDialogFragmentListener()
        setupCustomSingleChoiceDialogFragmentListener()
        setupCustomInputDialogFragmentListeners()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_VOLUME_FIRST, firstVolume)
        outState.putInt(KEY_VOLUME_SECOND, firstVolume)
    }

    // -----

    private fun showCustomDialogFragment() {
        CustomDialogFragment.show(supportFragmentManager, firstVolume)
    }

    private fun setupCustomDialogFragmentListener() {
        CustomDialogFragment.setupListener(supportFragmentManager, this) {
            this.firstVolume = it
            updateUi()
        }
    }

    // -----

    private fun showCustomSingleChoiceAlertDialog() {
        CustomSingleChoiceDialogFragment.show(supportFragmentManager, firstVolume)
    }

    private fun setupCustomSingleChoiceDialogFragmentListener() {
        CustomSingleChoiceDialogFragment.setupListener(supportFragmentManager, this) {
            this.firstVolume = it
            updateUi()
        }
    }

    // -----

    private fun showCustomInputDialogFragment(requestKey: String, volume: Int) {
        CustomInputDialogFragment.show(supportFragmentManager, volume, requestKey)
    }

    private fun setupCustomInputDialogFragmentListeners() {
        val listener: CustomInputDialogListener = { requestKey, volume ->
            when (requestKey) {
                KEY_FIRST_REQUEST_KEY -> this.firstVolume = volume
                KEY_SECOND_REQUEST_KEY -> this.secondVolume = volume
            }
            updateUi()
        }
        CustomInputDialogFragment.setupListener(supportFragmentManager, this, KEY_FIRST_REQUEST_KEY, listener)
        CustomInputDialogFragment.setupListener(supportFragmentManager, this, KEY_SECOND_REQUEST_KEY, listener)
    }

    // -----

    private fun updateUi() {
        binding.currentVolume1TextView.text = getString(R.string.current_volume_1, firstVolume)
        binding.currentVolume2TextView.text = getString(R.string.current_volume_2, secondVolume)
    }

    companion object {
        @JvmStatic private val KEY_VOLUME_FIRST = "KEY_VOLUME_FIRST"
        @JvmStatic private val KEY_VOLUME_SECOND = "KEY_VOLUME_SECOND"

        @JvmStatic private val KEY_FIRST_REQUEST_KEY = "KEY_VOLUME_FIRST_REQUEST_KEY"
        @JvmStatic private val KEY_SECOND_REQUEST_KEY = "KEY_VOLUME_SECOND_REQUEST_KEY"
    }

}