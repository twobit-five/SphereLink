package com.example.spherelink.Util

sealed class UiEvent {

    object PopBackStack : UiEvent()
    data class Navigate(val route: String): UiEvent()

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        ) : UiEvent()
}
