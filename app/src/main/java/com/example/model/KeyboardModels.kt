package com.example.model

enum class KeyboardMode {
    BENGALI,
    ENGLISH,
    SYMBOLS,
    SYMBOLS_SHIFT
}

enum class KeyType {
    CHARACTER,
    SHIFT,
    BACKSPACE,
    ENTER,
    SPACE,
    LANGUAGE_SWITCH,
    MODE_SWITCH,
    COMMA,
    PERIOD
}

data class KeyModel(
    val primaryLabel: String,
    val shiftedLabel: String? = null,
    val output: String = primaryLabel,
    val shiftedOutput: String? = shiftedLabel,
    val keyType: KeyType = KeyType.CHARACTER,
    val weight: Float = 1.0f,
    val altOptions: List<String> = emptyList()
)

data class KeyboardRow(
    val keys: List<KeyModel>
)

data class KeyboardLayoutData(
    val rows: List<KeyboardRow>
)
