package com.kbcoding.androiddevelopmentbasics.app.ui.splash

import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.LogCatLogger
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import com.kbcoding.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    accountsRepository: AccountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}