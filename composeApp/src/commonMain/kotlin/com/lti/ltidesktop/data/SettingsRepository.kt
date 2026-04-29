package com.lti.ltidesktop.data

import com.russhwolf.settings.Settings

class SettingsRepository(private val settings: Settings) {

    companion object {
        private const val KEY_HOST = "connection_host"
        private const val KEY_PORT = "connection_port"
        private const val KEY_AUTO_CONNECT = "auto_connect"
        private const val KEY_HISTORY_LIMIT = "history_limit"
        private const val KEY_TIMEOUT = "connection_timeout"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_OPACITY = "ui_opacity"
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_DOWNLOAD_PATH = "download_path"
    }

    fun getHost(): String = settings.getString(KEY_HOST, "192.168.1.100")
    fun setHost(host: String) = settings.putString(KEY_HOST, host)

    fun getPort(): String = settings.getString(KEY_PORT, "9876")
    fun setPort(port: String) = settings.putString(KEY_PORT, port)

    fun getAutoConnect(): Boolean = settings.getBoolean(KEY_AUTO_CONNECT, false)
    fun setAutoConnect(enabled: Boolean) = settings.putBoolean(KEY_AUTO_CONNECT, enabled)

    fun getHistoryLimit(): Int = settings.getInt(KEY_HISTORY_LIMIT, 1000)
    fun setHistoryLimit(limit: Int) = settings.putInt(KEY_HISTORY_LIMIT, limit)

    fun getTimeout(): Int = settings.getInt(KEY_TIMEOUT, 5000)
    fun setTimeout(timeout: Int) = settings.putInt(KEY_TIMEOUT, timeout)

    fun getFontSize(): Int = settings.getInt(KEY_FONT_SIZE, 13)
    fun setFontSize(size: Int) = settings.putInt(KEY_FONT_SIZE, size)

    fun getOpacity(): Float = settings.getFloat(KEY_OPACITY, 1.0f)
    fun setOpacity(opacity: Float) = settings.putFloat(KEY_OPACITY, opacity)

    fun getLanguage(): String = settings.getString(KEY_LANGUAGE, "English")
    fun setLanguage(language: String) = settings.putString(KEY_LANGUAGE, language)

    fun isDarkTheme(): Boolean = settings.getBoolean("is_dark_theme", true)
    fun setDarkTheme(isDark: Boolean) = settings.putBoolean("is_dark_theme", isDark)

    fun getDownloadPath(): String = settings.getString(
        KEY_DOWNLOAD_PATH,
        System.getProperty("user.home") + java.io.File.separator + "Downloads"
    )
    fun setDownloadPath(path: String) = settings.putString(KEY_DOWNLOAD_PATH, path)
}
