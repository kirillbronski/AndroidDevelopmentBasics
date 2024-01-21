package com.kbcoding.androiddevelopmentbasics.model

data class Cat(
    val id: Long,
    val name: String,
    val photoUrl: String,
    val description: String,
    val isFavorite: Boolean
)