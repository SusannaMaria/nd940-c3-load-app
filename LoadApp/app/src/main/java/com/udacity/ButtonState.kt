package com.udacity

/**
 * https://kotlinlang.org/docs/sealed-classes.html
 */
sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}