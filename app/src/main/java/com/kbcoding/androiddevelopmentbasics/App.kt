package com.kbcoding.androiddevelopmentbasics

import android.app.Application
import com.kbcoding.androiddevelopmentbasics.model.colors.InMemoryColorsRepository

class App: Application() {

    val models = listOf<Any>(
        InMemoryColorsRepository()
    )
}