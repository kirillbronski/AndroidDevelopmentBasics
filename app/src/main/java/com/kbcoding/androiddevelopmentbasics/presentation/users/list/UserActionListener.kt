package com.kbcoding.androiddevelopmentbasics.presentation.users.list

import com.kbcoding.androiddevelopmentbasics.model.User

interface UserActionListener {
        fun onUserMove(user: User, moveBy: Int)

        fun onUserDelete(user: User)

        fun onUserDetails(user: User)

        fun onUserFire(user: User)
    }