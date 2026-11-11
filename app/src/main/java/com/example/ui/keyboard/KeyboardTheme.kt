package com.example.ui.keyboard

import androidx.compose.ui.graphics.Color
import com.example.storage.ThemePreset

data class KeyboardThemeColors(
    val background: Color,
    val toolbarBackground: Color,
    val toolbarIcon: Color,
    val toolbarIconActive: Color,
    val keyBackground: Color,
    val keyBorder: Color,
    val keyText: Color,
    val keySubText: Color,
    val keyActionBackground: Color,
    val keyActionText: Color,
    val keyAccentBackground: Color,
    val keyAccentText: Color,
    val popupBackground: Color,
    val popupText: Color
)

object KeyboardThemes {

    fun getColors(theme: ThemePreset): KeyboardThemeColors {
        return when (theme) {
            ThemePreset.GBOARD_LIGHT -> KeyboardThemeColors(
                background = Color(0xFFECEFF1),
                toolbarBackground = Color(0xFFECEFF1),
                toolbarIcon = Color(0xFF5F6368),
                toolbarIconActive = Color(0xFF1A73E8),
                keyBackground = Color(0xFFFFFFFF),
                keyBorder = Color(0xFFD6D9DC),
                keyText = Color(0xFF202124),
                keySubText = Color(0xFF5F6368),
                keyActionBackground = Color(0xFFDADCE0),
                keyActionText = Color(0xFF3C4043),
                keyAccentBackground = Color(0xFF1A73E8),
                keyAccentText = Color(0xFFFFFFFF),
                popupBackground = Color(0xFFFFFFFF),
                popupText = Color(0xFF202124)
            )

            ThemePreset.GBOARD_DARK -> KeyboardThemeColors(
                background = Color(0xFF1F1F1F),
                toolbarBackground = Color(0xFF1F1F1F),
                toolbarIcon = Color(0xFF9AA0A6),
                toolbarIconActive = Color(0xFF8AB4F8),
                keyBackground = Color(0xFF2D2E30),
                keyBorder = Color(0xFF3C4043),
                keyText = Color(0xFFE8EAED),
                keySubText = Color(0xFF9AA0A6),
                keyActionBackground = Color(0xFF3C4043),
                keyActionText = Color(0xFFE8EAED),
                keyAccentBackground = Color(0xFF8AB4F8),
                keyAccentText = Color(0xFF202124),
                popupBackground = Color(0xFF2D2E30),
                popupText = Color(0xFFE8EAED)
            )

            ThemePreset.AMOLED_BLACK -> KeyboardThemeColors(
                background = Color(0xFF000000),
                toolbarBackground = Color(0xFF000000),
                toolbarIcon = Color(0xFFB0B0B0),
                toolbarIconActive = Color(0xFF64B5F6),
                keyBackground = Color(0xFF141416),
                keyBorder = Color(0xFF282828),
                keyText = Color(0xFFFFFFFF),
                keySubText = Color(0xFF8E8E93),
                keyActionBackground = Color(0xFF202022),
                keyActionText = Color(0xFFFFFFFF),
                keyAccentBackground = Color(0xFF1976D2),
                keyAccentText = Color(0xFFFFFFFF),
                popupBackground = Color(0xFF1A1A1A),
                popupText = Color(0xFFFFFFFF)
            )

            ThemePreset.COBALT_BLUE -> KeyboardThemeColors(
                background = Color(0xFF0D1B2A),
                toolbarBackground = Color(0xFF0D1B2A),
                toolbarIcon = Color(0xFF778DA9),
                toolbarIconActive = Color(0xFF48CAE4),
                keyBackground = Color(0xFF1B263B),
                keyBorder = Color(0xFF415A77),
                keyText = Color(0xFFE0E1DD),
                keySubText = Color(0xFF778DA9),
                keyActionBackground = Color(0xFF415A77),
                keyActionText = Color(0xFFE0E1DD),
                keyAccentBackground = Color(0xFF0077B6),
                keyAccentText = Color(0xFFFFFFFF),
                popupBackground = Color(0xFF1B263B),
                popupText = Color(0xFFFFFFFF)
            )

            ThemePreset.FOREST_MINT -> KeyboardThemeColors(
                background = Color(0xFF132A13),
                toolbarBackground = Color(0xFF132A13),
                toolbarIcon = Color(0xFF90A955),
                toolbarIconActive = Color(0xFFECF39E),
                keyBackground = Color(0xFF31572C),
                keyBorder = Color(0xFF4F772D),
                keyText = Color(0xFFECF39E),
                keySubText = Color(0xFF90A955),
                keyActionBackground = Color(0xFF4F772D),
                keyActionText = Color(0xFFECF39E),
                keyAccentBackground = Color(0xFF90A955),
                keyAccentText = Color(0xFF132A13),
                popupBackground = Color(0xFF31572C),
                popupText = Color(0xFFECF39E)
            )
        }
    }
}
