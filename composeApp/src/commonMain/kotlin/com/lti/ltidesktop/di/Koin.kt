package com.lti.ltidesktop.di

import com.lti.ltidesktop.presentation.AppViewModel
import com.lti.ltidesktop.data.SettingsRepository
import com.lti.ltidesktop.data.TerminalRepository
import com.lti.ltidesktop.network.LtiApiService
import com.lti.ltidesktop.ui.theme.ThemeManager
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Infrastructure
    single { 
        HttpClient {
            install(WebSockets)
        }
    }
    single { Settings() }

    // Data / Domain
    single { SettingsRepository(get()) }
    single { ThemeManager(get()) }
    single { LtiApiService(get()) }
    single { TerminalRepository(get()) }

    // UI
    viewModelOf(::AppViewModel)
}
