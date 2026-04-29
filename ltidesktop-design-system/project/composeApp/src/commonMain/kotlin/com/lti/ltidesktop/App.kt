package com.lti.ltidesktop

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import com.lti.ltidesktop.network.ConnectionState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.AppViewModel
import com.lti.ltidesktop.presentation.Screen
import com.lti.ltidesktop.ui.components.*
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.ThemeManager
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    vm: AppViewModel = koinViewModel(),
    themeManager: ThemeManager = koinInject()
) {
    val state by vm.state.collectAsState()

    var showSplash by remember { mutableStateOf(true) }

    LtiTheme(isDark = themeManager.isDark) {
        if (showSplash) {
            SplashScreen(onFinished = { showSplash = false })
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LtiTheme.colors.background)
                    .graphicsLayer(alpha = state.settings.opacity)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) {
                        when (keyEvent.key) {
                            Key.B -> {
                                vm.onEvent(AppEvent.ToggleSidebar)
                                true
                            }
                            Key.One -> {
                                vm.onEvent(AppEvent.NavigateTo(Screen.HOME))
                                true
                            }
                            Key.Two -> {
                                vm.onEvent(AppEvent.NavigateTo(Screen.CLI))
                                true
                            }
                            Key.Comma -> {
                                vm.onEvent(AppEvent.NavigateTo(Screen.SETTINGS))
                                true
                            }
                            else -> false
                        }
                    } else false
                }
        ) {
            Crossfade(
                targetState = state.connectionState == ConnectionState.CONNECTED,
                animationSpec = tween(durationMillis = 500)
            ) { isConnected ->
                if (isConnected) {
                    // Main App Structure
                    MainNavigation(
                        currentScreen = state.currentScreen,
                        isExpanded = state.isSidebarExpanded,
                        onEvent = vm::onEvent
                    ) {
                        AnimatedContent(
                            targetState = state.currentScreen,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                            }
                        ) { screen ->
                            when (screen) {
                                Screen.HOME -> HomeScreen(state, vm::onEvent)
                                Screen.CLI -> ConsoleScreen(state, vm.effect, vm::onEvent)
                                Screen.SETTINGS -> SettingsScreen(state, vm::onEvent)
                            }
                        }
                    }
                } else {
                    // Initial Connection Screen
                    ConnectScreen(
                        state = state,
                        onEvent = vm::onEvent
                    )
                }
            }
            }
        }
    }
}
