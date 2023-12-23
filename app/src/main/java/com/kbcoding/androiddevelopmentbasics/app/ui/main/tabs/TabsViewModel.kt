package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.Singletons
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.LogCatLogger
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import com.kbcoding.core.BaseViewModel
import kotlinx.coroutines.launch

class TabsViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

}