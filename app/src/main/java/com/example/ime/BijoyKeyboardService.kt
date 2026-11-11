package com.example.ime

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.MainActivity
import com.example.engine.BijoyCharacterMapper
import com.example.engine.PhysicalKeyboardHandler
import com.example.model.KeyModel
import com.example.model.KeyboardMode
import com.example.storage.KeyboardPreferences
import com.example.ui.keyboard.KeyboardScreen

class BijoyKeyboardService : InputMethodService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val store = ViewModelStore()

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore get() = store

    private lateinit var preferences: KeyboardPreferences
    private val physicalKeyboardHandler = PhysicalKeyboardHandler()

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(Bundle())
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        preferences = KeyboardPreferences.getInstance(this)
    }

    override fun onCreateInputView(): View {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@BijoyKeyboardService)
            setViewTreeSavedStateRegistryOwner(this@BijoyKeyboardService)
            setViewTreeViewModelStoreOwner(this@BijoyKeyboardService)

            setContent {
                val settings by preferences.settings.collectAsState()

                KeyboardScreen(
                    settings = settings,
                    onKeyTap = { key: KeyModel, isShift: Boolean ->
                        handleKeyInput(key, isShift, settings.currentMode)
                    },
                    onBackspace = {
                        handleBackspace()
                    },
                    onSpace = {
                        currentInputConnection?.commitText(" ", 1)
                    },
                    onEnter = {
                        handleEnter()
                    },
                    onLanguageSwitch = {
                        val nextMode = if (settings.currentMode == KeyboardMode.BENGALI) {
                            KeyboardMode.ENGLISH
                        } else {
                            KeyboardMode.BENGALI
                        }
                        preferences.setLastMode(nextMode)
                    },
                    onOpenSettings = {
                        val intent = Intent(this@BijoyKeyboardService, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(intent)
                    },
                    onSelectTheme = { theme ->
                        preferences.setTheme(theme)
                    },
                    onCursorMove = { delta ->
                        val keyCode = if (delta < 0) KeyEvent.KEYCODE_DPAD_LEFT else KeyEvent.KEYCODE_DPAD_RIGHT
                        sendDownUpKeyEvents(keyCode)
                    }
                )
            }
        }

        return composeView
    }

    private fun handleKeyInput(key: KeyModel, isShift: Boolean, mode: KeyboardMode) {
        val ic = currentInputConnection ?: return
        val output = if (isShift && key.shiftedOutput != null) key.shiftedOutput else key.output

        if (mode == KeyboardMode.BENGALI) {
            BijoyCharacterMapper.commitBijoyInput(output, ic)
        } else {
            ic.commitText(output, 1)
        }
    }

    private fun handleBackspace() {
        val ic = currentInputConnection ?: return
        val selectedText = ic.getSelectedText(0)
        if (!selectedText.isNullOrEmpty()) {
            ic.commitText("", 1)
        } else {
            ic.deleteSurroundingText(1, 0)
        }
    }

    private fun handleEnter() {
        val ic = currentInputConnection ?: return
        val info = currentInputEditorInfo
        if (info != null && (info.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0) {
            val action = info.imeOptions and EditorInfo.IME_MASK_ACTION
            if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
                ic.performEditorAction(action)
                return
            }
        }
        ic.commitText("\n", 1)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val settings = preferences.settings.value
        if (settings.physicalKeyboardEnabled) {
            val handled = physicalKeyboardHandler.onKeyDown(
                keyCode,
                event,
                settings.currentMode,
                currentInputConnection
            )
            if (handled) return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
        super.onDestroy()
    }
}
