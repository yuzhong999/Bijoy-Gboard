package com.example.ui.keyboard

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.engine.KeyboardLayouts
import com.example.model.KeyModel
import com.example.model.KeyType
import com.example.model.KeyboardMode
import com.example.storage.KeyboardSettingsState
import com.example.storage.ThemePreset

@Composable
fun KeyboardScreen(
    settings: KeyboardSettingsState,
    onKeyTap: (KeyModel, Boolean) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onLanguageSwitch: () -> Unit,
    onOpenSettings: () -> Unit,
    onSelectTheme: (ThemePreset) -> Unit,
    onCursorMove: (Int) -> Unit
) {
    val context = LocalContext.current
    val colors = remember(settings.theme) { KeyboardThemes.getColors(settings.theme) }

    // Keyboard state
    var currentMode by remember(settings.currentMode) { mutableStateOf(settings.currentMode) }
    var isShift by remember { mutableStateOf(false) }

    // Secondary views
    var showEmojiPicker by remember { mutableStateOf(false) }
    var showClipboardDrawer by remember { mutableStateOf(false) }
    var showThemeMenu by remember { mutableStateOf(false) }

    // Long press alternate dialog
    var activeAltKey by remember { mutableStateOf<KeyModel?>(null) }

    // Vibrator & Audio
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager }

    fun playFeedback() {
        if (settings.hapticFeedback && vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(20)
            }
        }
        if (settings.soundFeedback) {
            audioManager?.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    val layoutData = remember(currentMode, isShift, settings.showNumberRow) {
        when (currentMode) {
            KeyboardMode.BENGALI -> KeyboardLayouts.getBengaliLayout(isShift, settings.showNumberRow)
            KeyboardMode.ENGLISH -> KeyboardLayouts.getEnglishLayout(isShift, settings.showNumberRow)
            KeyboardMode.SYMBOLS -> KeyboardLayouts.getSymbolsLayout(false)
            KeyboardMode.SYMBOLS_SHIFT -> KeyboardLayouts.getSymbolsLayout(true)
        }
    }

    val baseRowHeight = (46 * settings.keyboardHeightScale).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
    ) {
        // Gboard Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.toolbarBackground)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    playFeedback()
                    onOpenSettings()
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = colors.toolbarIcon,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box {
                    IconButton(onClick = {
                        playFeedback()
                        showThemeMenu = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Themes",
                            tint = colors.toolbarIcon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showThemeMenu,
                        onDismissRequest = { showThemeMenu = false },
                        modifier = Modifier.background(colors.popupBackground)
                    ) {
                        ThemePreset.values().forEach { preset ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = preset.displayName,
                                        color = colors.popupText,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    onSelectTheme(preset)
                                    showThemeMenu = false
                                }
                            )
                        }
                    }
                }

                IconButton(onClick = {
                    playFeedback()
                    showClipboardDrawer = !showClipboardDrawer
                    showEmojiPicker = false
                }) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = "Clipboard",
                        tint = if (showClipboardDrawer) colors.toolbarIconActive else colors.toolbarIcon,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = {
                    playFeedback()
                    showEmojiPicker = !showEmojiPicker
                    showClipboardDrawer = false
                }) {
                    Icon(
                        imageVector = Icons.Default.EmojiEmotions,
                        contentDescription = "Emoji",
                        tint = if (showEmojiPicker) colors.toolbarIconActive else colors.toolbarIcon,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Cursor Left / Right arrows for precise editing
                IconButton(onClick = {
                    playFeedback()
                    onCursorMove(-1)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Cursor Left",
                        tint = colors.toolbarIcon,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = {
                    playFeedback()
                    onCursorMove(1)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Cursor Right",
                        tint = colors.toolbarIcon,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Quick language badge
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.keyActionBackground)
                    .clickable {
                        playFeedback()
                        currentMode = if (currentMode == KeyboardMode.BENGALI) KeyboardMode.ENGLISH else KeyboardMode.BENGALI
                        onLanguageSwitch()
                    }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (currentMode == KeyboardMode.BENGALI) "বাংলা" else "EN",
                    color = colors.keyActionText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Secondary panels: Clipboard Drawer & Emoji Picker
        AnimatedVisibility(
            visible = showClipboardDrawer,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            ClipboardDrawer(
                colors = colors,
                onPasteText = { text ->
                    playFeedback()
                    onKeyTap(KeyModel(primaryLabel = text, output = text), false)
                },
                onClose = { showClipboardDrawer = false }
            )
        }

        AnimatedVisibility(
            visible = showEmojiPicker,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            EmojiPicker(
                colors = colors,
                onEmojiSelected = { emoji ->
                    playFeedback()
                    onKeyTap(KeyModel(primaryLabel = emoji, output = emoji), false)
                },
                onBackspace = {
                    playFeedback()
                    onBackspace()
                },
                onClose = { showEmojiPicker = false }
            )
        }

        // Main Keyboard Matrix (only visible when secondary full panels are closed)
        if (!showEmojiPicker) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                layoutData.rows.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(baseRowHeight),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.keys.forEach { key ->
                            KeyView(
                                key = key,
                                isShift = isShift,
                                colors = colors,
                                showBorders = settings.showKeyBorders,
                                modifier = Modifier.weight(key.weight),
                                onClick = {
                                    playFeedback()
                                    when (key.keyType) {
                                        KeyType.SHIFT -> {
                                            if (currentMode == KeyboardMode.SYMBOLS) {
                                                currentMode = KeyboardMode.SYMBOLS_SHIFT
                                            } else if (currentMode == KeyboardMode.SYMBOLS_SHIFT) {
                                                currentMode = KeyboardMode.SYMBOLS
                                            } else {
                                                isShift = !isShift
                                            }
                                        }
                                        KeyType.BACKSPACE -> onBackspace()
                                        KeyType.SPACE -> onSpace()
                                        KeyType.ENTER -> onEnter()
                                        KeyType.LANGUAGE_SWITCH -> {
                                            currentMode = if (currentMode == KeyboardMode.BENGALI) KeyboardMode.ENGLISH else KeyboardMode.BENGALI
                                            onLanguageSwitch()
                                        }
                                        KeyType.MODE_SWITCH -> {
                                            currentMode = if (currentMode == KeyboardMode.BENGALI || currentMode == KeyboardMode.ENGLISH) {
                                                KeyboardMode.SYMBOLS
                                            } else {
                                                settings.currentMode
                                            }
                                        }
                                        else -> {
                                            onKeyTap(key, isShift)
                                            // Reset shift after typing a letter if in Shift mode
                                            if (isShift && currentMode != KeyboardMode.SYMBOLS_SHIFT) {
                                                isShift = false
                                            }
                                        }
                                    }
                                },
                                onLongClick = {
                                    if (key.altOptions.isNotEmpty()) {
                                        playFeedback()
                                        activeAltKey = key
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Long press alternate character dialog
    if (activeAltKey != null) {
        val altKey = activeAltKey!!
        Dialog(onDismissRequest = { activeAltKey = null }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = colors.popupBackground,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    altKey.altOptions.forEach { altChar ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.keyBackground)
                                .clickable {
                                    playFeedback()
                                    onKeyTap(KeyModel(primaryLabel = altChar, output = altChar), false)
                                    activeAltKey = null
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = altChar,
                                fontSize = 20.sp,
                                color = colors.keyText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyView(
    key: KeyModel,
    isShift: Boolean,
    colors: KeyboardThemeColors,
    showBorders: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val isActionKey = key.keyType in listOf(
        KeyType.SHIFT,
        KeyType.BACKSPACE,
        KeyType.ENTER,
        KeyType.MODE_SWITCH,
        KeyType.LANGUAGE_SWITCH
    )
    val isEnterKey = key.keyType == KeyType.ENTER

    val bg = when {
        isEnterKey -> colors.keyAccentBackground
        isActionKey -> colors.keyActionBackground
        else -> colors.keyBackground
    }

    val textColor = when {
        isEnterKey -> colors.keyAccentText
        isActionKey -> colors.keyActionText
        else -> colors.keyText
    }

    val displayLabel = if (isShift && key.shiftedLabel != null) key.shiftedLabel else key.primaryLabel

    Box(
        modifier = modifier
            .padding(horizontal = 1.dp, vertical = 2.dp)
            .shadow(
                elevation = if (isEnterKey) 2.dp else 1.dp,
                shape = RoundedCornerShape(6.dp),
                clip = false
            )
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .then(
                if (showBorders && !isEnterKey) {
                    Modifier.border(0.5.dp, colors.keyBorder, RoundedCornerShape(6.dp))
                } else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = displayLabel,
                fontSize = when {
                    key.keyType == KeyType.SPACE -> 13.sp
                    key.keyType == KeyType.MODE_SWITCH -> 12.sp
                    key.primaryLabel.length > 2 -> 13.sp
                    else -> 19.sp
                },
                fontWeight = if (isEnterKey || isActionKey) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )

            // Show tiny hint of shifted key or alternate option if present
            if (!isShift && key.shiftedLabel != null && key.keyType == KeyType.CHARACTER && key.primaryLabel != key.shiftedLabel) {
                Text(
                    text = key.shiftedLabel,
                    fontSize = 9.sp,
                    color = colors.keySubText,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }
}
