package com.kbcoding.androiddevelopmentbasics.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kbcoding.androiddevelopmentbasics.Options
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomTitle
import com.kbcoding.androiddevelopmentbasics.contract.navigator
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentBoxSelectionBinding
import com.kbcoding.androiddevelopmentbasics.databinding.ItemBoxBinding
import java.lang.Math.max
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : BaseFragment<FragmentBoxSelectionBinding>(), HasCustomTitle {


    private lateinit var options: Options

    private var timerStartTimestamp by Delegates.notNull<Long>()
    private var boxIndex by Delegates.notNull<Int>()
    private var alreadyDone = false

    private var timerHandler: TimerHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            options = arguments?.getSerializable(ARG_OPTIONS, Options::class.java)
                ?: throw IllegalArgumentException("Can't launch BoxSelectionActivity without options")
        } else {
            options = arguments?.getParcelable(ARG_OPTIONS)
                ?: throw IllegalArgumentException("Can't launch BoxSelectionActivity without options")
        }

        boxIndex = savedInstanceState?.getInt(KEY_INDEX) ?: Random.nextInt(options.boxCount)

        timerHandler = if (options.isTimerEnabled) {
            TimerHandler()
        } else {
            null
        }
        timerHandler?.onCreate(savedInstanceState)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBoxSelectionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createBoxes()
    }

    private fun createBoxes() {
        val boxBindings = (0 until options.boxCount).map { index ->
            val boxBinding = ItemBoxBinding.inflate(layoutInflater)
            boxBinding.root.id = View.generateViewId()
            boxBinding.boxTitleTextView.text = getString(R.string.box_title, index + 1)
            boxBinding.root.setOnClickListener { view -> onBoxSelected(view) }
            boxBinding.root.tag = index
            binding.root.addView(boxBinding.root)
            boxBinding
        }

        binding.flow.referencedIds = boxBindings.map { it.root.id }.toIntArray()
    }

    private fun onBoxSelected(view: View) {
        if (view.tag as Int == boxIndex) {
            alreadyDone = true // disabling timer if the user made a right choice
            navigator().showCongratulationsScreen()
        } else {
            Toast.makeText(context, R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, boxIndex)
        timerHandler?.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        timerHandler?.onStart()
    }

    override fun onStop() {
        super.onStop()
        timerHandler?.onStop()
    }


    override fun getTitleRes(): Int = R.string.box

    private fun updateTimerUi() {
        if (getRemainingSeconds() >= 0) {
            binding.tvTimer.visibility = View.VISIBLE
            binding.tvTimer.text = getString(R.string.timer_value, getRemainingSeconds())
        } else {
            binding.tvTimer.visibility = View.GONE
        }
    }

    private fun showTimerEndDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.the_end))
            .setMessage(getString(R.string.timer_end_message))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> navigator().goBack() }
            .create()
        dialog.show()
    }

    private fun getRemainingSeconds(): Long {
        val finishedAt = timerStartTimestamp + TIMER_DURATION
        return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
    }


    inner class TimerHandler {

        private var timer: CountDownTimer? = null

        fun onCreate(savedInstanceState: Bundle?) {
            timerStartTimestamp = savedInstanceState?.getLong(KEY_START_TIMESTAMP)
                ?: System.currentTimeMillis()
            alreadyDone = savedInstanceState?.getBoolean(KEY_ALREADY_DONE) ?: false
        }

        fun onSaveInstanceState(outState: Bundle) {
            outState.putLong(KEY_START_TIMESTAMP, timerStartTimestamp)
            outState.putBoolean(KEY_ALREADY_DONE, alreadyDone)
        }

        fun onStart() {
            if (alreadyDone) return
            // timer is paused when app is minimized (onTick is not called), but we remember the initial
            // timestamp when the screen has been launched, so actually the dialog is displayed in 10
            // seconds after the initial timestamp anyway. If the app is minimized for more than 10 seconds
            // the dialog is shown immediately after restoring the app.
            timer = object : CountDownTimer(getRemainingSeconds() * 1000, 1000) {
                override fun onFinish() {
                    updateTimerUi()
                    showTimerEndDialog()
                }

                override fun onTick(millisUntilFinished: Long) {
                    updateTimerUi()
                }
            }
            updateTimerUi()
            timer?.start()
        }

        fun onStop() {
            timer?.cancel()
            timer = null
        }
    }


    companion object {
        @JvmStatic
        private val ARG_OPTIONS = "EXTRA_OPTIONS"
        @JvmStatic
        private val KEY_INDEX = "KEY_INDEX"
        @JvmStatic
        private val KEY_START_TIMESTAMP = "KEY_START_TIMESTAMP"
        @JvmStatic
        private val KEY_ALREADY_DONE = "KEY_ALREADY_DONE"
        @JvmStatic
        private val TIMER_DURATION = 10_000L

        @JvmStatic
        fun newInstance(options: Options) =
            BoxSelectionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_OPTIONS, options)
                }
            }
    }
}