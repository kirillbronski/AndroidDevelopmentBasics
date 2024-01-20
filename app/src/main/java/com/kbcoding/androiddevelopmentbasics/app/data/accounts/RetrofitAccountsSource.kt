package com.kbcoding.androiddevelopmentbasics.app.data.accounts

import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.SignUpData
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.SignInRequestEntity
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.SignUpRequestEntity
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.UpdateUsernameRequestEntity
import com.kbcoding.androiddevelopmentbasics.app.data.base.BaseRetrofitSource
import com.kbcoding.androiddevelopmentbasics.app.data.base.RetrofitConfig
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitAccountsSource @Inject constructor(
    config: RetrofitConfig
) : BaseRetrofitSource(config), AccountsSource {

    private val accountsApi = retrofit.create(AccountsApi::class.java)

    override suspend fun signIn(
        email: String,
        password: String
    ): String = wrapRetrofitExceptions {
        delay(1000)
        val signInRequestEntity = SignInRequestEntity(email, password)
        accountsApi.signIn(signInRequestEntity).token
    }

    override suspend fun signUp(
        signUpData: SignUpData
    ) = wrapRetrofitExceptions {
        delay(1000)
        val signUpRequestEntity = SignUpRequestEntity(
            signUpData.email,
            signUpData.username,
            signUpData.password
        )
        accountsApi.signUp(signUpRequestEntity)
    }

    override suspend fun getAccount(): Account = wrapRetrofitExceptions {
        delay(1000)
        accountsApi.getAccount().toAccount()
    }

    override suspend fun setUsername(
        username: String
    ) = wrapRetrofitExceptions {
        delay(1000)
        val updateUsernameRequestEntity = UpdateUsernameRequestEntity(username)
        accountsApi.setUsername(updateUsernameRequestEntity)
    }

}