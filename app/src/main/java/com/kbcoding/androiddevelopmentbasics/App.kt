package com.kbcoding.androiddevelopmentbasics

import android.app.Application
import com.kbcoding.androiddevelopmentbasics.model.colors.InMemoryColorsRepository
import com.kbcoding.core.model.Repository
import com.kbcoding.core.presentation.BaseApplication

class App : Application(), BaseApplication {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val repositories: List<Repository> = listOf<Repository>(
        InMemoryColorsRepository()
    )
}