package com.kbcoding.androiddevelopmentbasics.app.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.AuthException
import com.kbcoding.androiddevelopmentbasics.app.domain.BackendException
import com.kbcoding.androiddevelopmentbasics.app.domain.ConnectionException
import com.kbcoding.androiddevelopmentbasics.app.domain.Error
import com.kbcoding.androiddevelopmentbasics.app.domain.Pending
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.databinding.PartResultViewBinding
import com.kbcoding.core.BaseFragment

class ResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: PartResultViewBinding
    private var tryAgainAction: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_result_view, this, true)
        binding = PartResultViewBinding.bind(this)
    }

    /**
     * Assign an action for 'Try Again' button.
     */
    fun setTryAgainAction(action: () -> Unit) {
        this.tryAgainAction = action
    }

    /**
     * Set the current result to be displayed to the user.
     */
    fun <T> setResult(fragment: BaseFragment<*>, result: ResponseResult<T>) {
        binding.messageTextView.isVisible = result is Error
        binding.errorButton.isVisible = result is Error
        binding.progressBar.isVisible = result is Pending
        if (result is Error) {
            Log.e(javaClass.simpleName, "Error", result.error)
            val message = when (result.error) {
                is ConnectionException -> context.getString(R.string.connection_error)
                is AuthException -> context.getString(R.string.auth_error)
                is BackendException -> result.error.message
                else -> context.getString(R.string.internal_error)
            }
            binding.messageTextView.text = message
            if (result.error is AuthException) {
                renderLogoutButton(fragment)
            } else {
                renderTryAgainButton()
            }
        }
    }

    private fun renderLogoutButton(fragment: BaseFragment<*>) {
        binding.errorButton.setOnClickListener {
            fragment.logout()
        }
        binding.errorButton.setText(R.string.action_logout)
    }

    private fun renderTryAgainButton() {
        binding.errorButton.setOnClickListener { tryAgainAction?.invoke() }
        binding.errorButton.setText(R.string.action_try_again)
    }

}