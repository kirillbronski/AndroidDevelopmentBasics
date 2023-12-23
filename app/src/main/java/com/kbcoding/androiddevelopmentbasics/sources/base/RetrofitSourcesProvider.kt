package com.kbcoding.androiddevelopmentbasics.sources.base

import com.kbcoding.androiddevelopmentbasics.app.model.SourcesProvider
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.BoxesSource
import com.kbcoding.androiddevelopmentbasics.sources.accounts.RetrofitAccountsSource
import com.kbcoding.androiddevelopmentbasics.sources.boxes.RetrofitBoxesSource

/**
 * Creating sources based on Retrofit + Moshi.
 */
class RetrofitSourcesProvider(
    private val config: RetrofitConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        return RetrofitAccountsSource(config)
    }

    override fun getBoxesSource(): BoxesSource {
        return RetrofitBoxesSource(config)
    }

}