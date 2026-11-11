package com.example.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.model.KeyboardMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemePreset(val displayName: String) {
    GBOARD_LIGHT("Gboard Light"),
    GBOARD_DARK("Gboard Dark"),
    AMOLED_BLACK("AMOLED Pitch Black"),
    COBALT_BLUE("Cobalt Blue"),
    FOREST_MINT("Forest Mint")
}

data class KeyboardSettingsState(
    val theme: ThemePreset = ThemePreset.GBOARD_LIGHT,
    val hapticFeedback: Boolean = true,
    val soundFeedback: Boolean = false,
    val showKeyBorders: Boolean = true,
    val showNumberRow: Boolean = true,
    val keyboardHeightScale: Float = 1.0f,
    val physicalKeyboardEnabled: Boolean = true,
    val currentMode: KeyboardMode = KeyboardMode.BENGALI
)

class KeyboardPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bijoy_gboard_preferences", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<KeyboardSettingsState> = _settings.asStateFlow()

    private fun loadSettings(): KeyboardSettingsState {
        val themeName = prefs.getString(KEY_THEME, ThemePreset.GBOARD_LIGHT.name)
        val theme = try {
            ThemePreset.valueOf(themeName ?: ThemePreset.GBOARD_LIGHT.name)
        } catch (_: Exception) {
            ThemePreset.GBOARD_LIGHT
        }

        val modeName = prefs.getString(KEY_LAST_MODE, KeyboardMode.BENGALI.name)
        val mode = try {
            KeyboardMode.valueOf(modeName ?: KeyboardMode.BENGALI.name)
        } catch (_: Exception) {
            KeyboardMode.BENGALI
        }

        return KeyboardSettingsState(
            theme = theme,
            hapticFeedback = prefs.getBoolean(KEY_HAPTIC, true),
            soundFeedback = prefs.getBoolean(KEY_SOUND, false),
            showKeyBorders = prefs.getBoolean(KEY_KEY_BORDERS, true),
            showNumberRow = prefs.getBoolean(KEY_NUMBER_ROW, true),
            keyboardHeightScale = prefs.getFloat(KEY_HEIGHT_SCALE, 1.0f),
            physicalKeyboardEnabled = prefs.getBoolean(KEY_PHYSICAL_KB, true),
            currentMode = mode
        )
    }

    fun setTheme(theme: ThemePreset) {
        prefs.edit().putString(KEY_THEME, theme.name).apply()
        _settings.value = _settings.value.copy(theme = theme)
    }

    fun setHapticFeedback(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTIC, enabled).apply()
        _settings.value = _settings.value.copy(hapticFeedback = enabled)
    }

    fun setSoundFeedback(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply()
        _settings.value = _settings.value.copy(soundFeedback = enabled)
    }

    fun setShowKeyBorders(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_KEY_BORDERS, enabled).apply()
        _settings.value = _settings.value.copy(showKeyBorders = enabled)
    }

    fun setShowNumberRow(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NUMBER_ROW, enabled).apply()
        _settings.value = _settings.value.copy(showNumberRow = enabled)
    }

    fun setKeyboardHeightScale(scale: Float) {
        prefs.edit().putFloat(KEY_HEIGHT_SCALE, scale).apply()
        _settings.value = _settings.value.copy(keyboardHeightScale = scale)
    }

    fun setPhysicalKeyboardEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PHYSICAL_KB, enabled).apply()
        _settings.value = _settings.value.copy(physicalKeyboardEnabled = enabled)
    }

    fun setLastMode(mode: KeyboardMode) {
        prefs.edit().putString(KEY_LAST_MODE, mode.name).apply()
        _settings.value = _settings.value.copy(currentMode = mode)
    }

    companion object {
        private const val KEY_THEME = "pref_theme"
        private const val KEY_HAPTIC = "pref_haptic"
        private const val KEY_SOUND = "pref_sound"
        private const val KEY_KEY_BORDERS = "pref_key_borders"
        private const val KEY_NUMBER_ROW = "pref_number_row"
        private const val KEY_HEIGHT_SCALE = "pref_height_scale"
        private const val KEY_PHYSICAL_KB = "pref_physical_keyboard"
        private const val KEY_LAST_MODE = "pref_last_mode"

        @Volatile
        private var instance: KeyboardPreferences? = null

        fun getInstance(context: Context): KeyboardPreferences {
            return instance ?: synchronized(this) {
                instance ?: KeyboardPreferences(context.applicationContext).also { instance = it }
            }
        }
    }
}
