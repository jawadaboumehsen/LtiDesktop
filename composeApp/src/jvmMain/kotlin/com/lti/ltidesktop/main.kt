package com.lti.ltidesktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.lti.ltidesktop.di.appModule
import org.koin.compose.KoinApplication

fun main() = application {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val windowState = rememberWindowState(
            size = DpSize(1100.dp, 720.dp),
        )
        Window(
            onCloseRequest = ::exitApplication,
            title = "LtiDesktop — Remote Console",
            state = windowState,
        ) {
            App()
        }
    }
}