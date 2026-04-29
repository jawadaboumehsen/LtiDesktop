package com.lti.ltidesktop.presentation

sealed interface AppEffect {
    data class ShowToast(val message: String) : AppEffect
    object ScrollToBottom : AppEffect
    data class UpdateInput(val text: String) : AppEffect
}
