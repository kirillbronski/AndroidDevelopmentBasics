package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.utils.share
import kotlinx.coroutines.launch

class TabsViewModel(
    private val accountsSource: AccountsSource
) : ViewModel() {

}