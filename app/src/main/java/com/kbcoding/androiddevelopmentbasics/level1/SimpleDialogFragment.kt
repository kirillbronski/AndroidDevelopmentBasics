package com.kbcoding.androiddevelopmentbasics.level1

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.showToast

class SimpleDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setIcon(R.mipmap.ic_launcher_round)
            .setTitle(R.string.default_alert_title)
            .setMessage(R.string.default_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "Dialog dismissed")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        showToast(R.string.dialog_cancelled)
    }

    companion object {
        @JvmStatic
        val TAG = SimpleDialogFragment::class.java.simpleName

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        @JvmStatic
        val KEY_RESPONSE = "RESPONSE"
    }
}