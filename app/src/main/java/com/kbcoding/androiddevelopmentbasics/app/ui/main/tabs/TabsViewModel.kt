package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs

import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.LogCatLogger
import com.kbcoding.androiddevelopmentbasics.app.utils.logger.Logger
import com.kbcoding.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    accountsRepository: AccountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

}