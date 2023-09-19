package com.kbcoding.androiddevelopmentbasics.presentation.changeColor

import com.kbcoding.androiddevelopmentbasics.model.colors.NamedColor

data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)