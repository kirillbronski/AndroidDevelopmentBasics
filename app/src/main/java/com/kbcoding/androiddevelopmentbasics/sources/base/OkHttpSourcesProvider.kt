package com.kbcoding.androiddevelopmentbasics.sources.base

import com.kbcoding.androiddevelopmentbasics.app.model.SourcesProvider
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsSource
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.BoxesSource
import com.kbcoding.androiddevelopmentbasics.sources.accounts.OkHttpAccountsSource
import com.kbcoding.androiddevelopmentbasics.sources.boxes.OkHttpBoxesSource

/**
 * Creating sources based on OkHttp + GSON.
 */
class OkHttpSourcesProvider(
    private val config: OkHttpConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        return OkHttpAccountsSource(config)
    }

    override fun getBoxesSource(): BoxesSource {
        return OkHttpBoxesSource(config)
    }

}