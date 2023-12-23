package com.kbcoding.androiddevelopmentbasics.sources.accounts

import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.entities.SignUpData
import com.kbcoding.androiddevelopmentbasics.sources.accounts.entities.SignInRequestEntity
import com.kbcoding.androiddevelopmentbasics.sources.accounts.entities.SignUpRequestEntity
import com.kbcoding.androiddevelopmentbasics.sources.accounts.entities.UpdateUsernameRequestEntity
import com.kbcoding.androiddevelopmentbasics.sources.base.BaseRetrofitSource
import com.kbcoding.androiddevelopmentbasics.sources.base.RetrofitConfig
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