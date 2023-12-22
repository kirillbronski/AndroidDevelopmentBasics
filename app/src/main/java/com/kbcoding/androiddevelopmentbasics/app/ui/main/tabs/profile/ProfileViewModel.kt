package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.Singletons
import com.kbcoding.androiddevelopmentbasics.app.model.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.utils.MutableLiveEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.LogCatLogger
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.publishEvent
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import com.kbcoding.core.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _account = MutableLiveData<ResponseResult<Account>>()
    val account = _account.share()

    init {
        viewModelScope.launch {
            accountsRepository.getAccount().collect {
                _account.value = it
            }
        }
    }

    fun reload() {
        accountsRepository.reloadAccount()
    }

}