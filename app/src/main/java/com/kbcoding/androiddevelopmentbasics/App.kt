package com.kbcoding.androiddevelopmentbasics

import android.app.Application
import com.kbcoding.androiddevelopmentbasics.model.UsersService

class App : Application() {

    val usersService = UsersService()
}