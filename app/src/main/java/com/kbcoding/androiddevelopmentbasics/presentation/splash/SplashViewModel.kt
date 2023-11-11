package com.kbcoding.androiddevelopmentbasics.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.domain.repository.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.utils.share
import kotlinx.coroutines.launch

class SplashViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}