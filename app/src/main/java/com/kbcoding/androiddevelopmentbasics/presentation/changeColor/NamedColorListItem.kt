package com.kbcoding.androiddevelopmentbasics.presentation.changeColor

import com.kbcoding.androiddevelopmentbasics.model.colors.NamedColor

/**
 * Represents list item for the color; it may be selected or not
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)