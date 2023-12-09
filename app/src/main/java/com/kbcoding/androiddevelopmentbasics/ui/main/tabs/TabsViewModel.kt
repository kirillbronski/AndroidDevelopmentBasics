package com.kbcoding.androiddevelopmentbasics.ui.main.tabs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbcoding.androiddevelopmentbasics.domain.repository.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.utils.share
import kotlinx.coroutines.launch

class TabsViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _showAdminTab = MutableLiveData<Boolean>()
    val showAdminTab = _showAdminTab.share()

    init {
        viewModelScope.launch {
            accountsRepository.getAccount().collect {
                _showAdminTab.value = it?.isAdmin() == true
            }
        }
    }
}