package com.example.engine

import android.view.KeyEvent
import android.view.inputmethod.InputConnection
import com.example.model.KeyboardMode

class PhysicalKeyboardHandler {

    /**
     * Intercepts physical key down events and maps them according to current keyboard mode.
     * Returns true if event was consumed.
     */
    fun onKeyDown(
        keyCode: Int,
        event: KeyEvent,
        currentMode: KeyboardMode,
        ic: InputConnection?
    ): Boolean {
        if (ic == null) return false

        // Standard navigation and control keys
        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> {
                ic.deleteSurroundingText(1, 0)
                return true
            }
            KeyEvent.KEYCODE_FORWARD_DEL -> {
                ic.deleteSurroundingText(0, 1)
                return true
            }
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                ic.commitText("\n", 1)
                return true
            }
            KeyEvent.KEYCODE_SPACE -> {
                ic.commitText(" ", 1)
                return true
            }
            KeyEvent.KEYCODE_TAB -> {
                ic.commitText("\t", 1)
                return true
            }
        }

        // If in English or Symbol mode, let standard Android IME handle physical QWERTY keys natively
        if (currentMode != KeyboardMode.BENGALI) {
            return false
        }

        // In Bengali mode: Map physical key to Bijoy Bengali character
        val isShift = event.isShiftPressed xor event.isCapsLockOn

        val printableChar: Char? = when (keyCode) {
            in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> {
                ('a'.code + (keyCode - KeyEvent.KEYCODE_A)).toChar()
            }
            in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                ('0'.code + (keyCode - KeyEvent.KEYCODE_0)).toChar()
            }
            KeyEvent.KEYCODE_SEMICOLON -> ';'
            KeyEvent.KEYCODE_APOSTROPHE -> '\''
            KeyEvent.KEYCODE_COMMA -> ','
            KeyEvent.KEYCODE_PERIOD -> '.'
            KeyEvent.KEYCODE_SLASH -> '/'
            else -> null
        }

        if (printableChar != null) {
            val consumed = BijoyCharacterMapper.processHardwareKey(printableChar, isShift, ic)
            if (consumed) {
                return true
            }
        }

        return false
    }
}
