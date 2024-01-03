package com.kbcoding.androiddevelopmentbasics.ui

import android.content.Context
import com.kbcoding.androiddevelopmentbasics.R

class Year(
    val value: Int?,
    private val message: String
) {

    constructor(context: Context, year: Int?) : this(
        value = year,
        message = year?.toString() ?: context.getString(R.string.all)
    )

    override fun toString(): String {
        return message
    }
}