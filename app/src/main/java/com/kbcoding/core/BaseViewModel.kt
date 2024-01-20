package com.kbcoding.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.AuthException
import com.kbcoding.androiddevelopmentbasics.app.domain.BackendException
import com.kbcoding.androiddevelopmentbasics.app.domain.ConnectionException
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.MutableUnitLiveEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel(
    val accountsRepository: AccountsRepository,
    val logger: Logger
) : ViewModel() {

    private val _showErrorMessageResEvent = MutableLiveEvent<Int>()
    val showErrorMessageResEvent = _showErrorMessageResEvent.share()

    private val _showErrorMessageEvent = MutableLiveEvent<String>()
    val showErrorMessageEvent = _showErrorMessageEvent.share()

    private val _showAuthErrorAndRestartEvent = MutableUnitLiveEvent()
    val showAuthErrorAndRestartEvent = _showAuthErrorAndRestartEvent.share()

    fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: ConnectionException) {
                logError(e)
                _showErrorMessageResEvent.publishEvent(R.string.connection_error)
            } catch (e: BackendException) {
                logError(e)
                _showErrorMessageEvent.publishEvent(e.message ?: "")
            } catch (e: AuthException) {
                logError(e)
                _showAuthErrorAndRestartEvent.publishEvent()
            } catch (e: Exception) {
                logError(e)
                _showErrorMessageResEvent.publishEvent(R.string.internal_error)
            }
        }
    }

    fun logError(e: Throwable) {
        logger.error(javaClass.simpleName, e)
    }

    fun logout() {
        accountsRepository.logout()
    }

    protected fun showErrorMessage(messageRes: Int) =
        _showErrorMessageResEvent.publishEvent(messageRes)

}