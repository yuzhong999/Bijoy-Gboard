package com.example.engine

import com.example.model.KeyModel
import com.example.model.KeyType
import com.example.model.KeyboardLayoutData
import com.example.model.KeyboardRow

object KeyboardLayouts {

    fun getBengaliLayout(isShift: Boolean, showNumberRow: Boolean): KeyboardLayoutData {
        val rows = mutableListOf<KeyboardRow>()

        // Row 0: Number row
        if (showNumberRow) {
            val numKeys = listOf(
                KeyModel("১", "!", "১", "!", altOptions = listOf("1", "!")),
                KeyModel("২", "@", "২", "@", altOptions = listOf("2", "@")),
                KeyModel("৩", "#", "৩", "#", altOptions = listOf("3", "#")),
                KeyModel("৪", "৳", "৪", "৳", altOptions = listOf("4", "৳", "$")),
                KeyModel("৫", "%", "৫", "%", altOptions = listOf("5", "%")),
                KeyModel("৬", "ঃ", "৬", "ঃ", altOptions = listOf("6", "^")),
                KeyModel("৭", "&", "৭", "&", altOptions = listOf("7", "&")),
                KeyModel("৮", "*", "৮", "*", altOptions = listOf("8", "*")),
                KeyModel("৯", "(", "৯", "(", altOptions = listOf("9", "(")),
                KeyModel("০", ")", "০", ")", altOptions = listOf("0", ")"))
            )
            rows.add(KeyboardRow(numKeys))
        }

        // Row 1 (Q - P)
        val row1 = listOf(
            KeyModel("ঙ", "ং", "ঙ", "ং", altOptions = listOf("1")),
            KeyModel("য", "য়", "য", "য়", altOptions = listOf("2")),
            KeyModel("ড", "ঢ", "ড", "ঢ", altOptions = listOf("3")),
            KeyModel("প", "ফ", "প", "ফ", altOptions = listOf("4")),
            KeyModel("ট", "ঠ", "ট", "ঠ", altOptions = listOf("5")),
            KeyModel("চ", "ছ", "চ", "ছ", altOptions = listOf("6")),
            KeyModel("জ", "ঝ", "জ", "ঝ", altOptions = listOf("7")),
            KeyModel("হ", "ঞ", "হ", "ঞ", altOptions = listOf("8")),
            KeyModel("গ", "ঘ", "গ", "ঘ", altOptions = listOf("9")),
            KeyModel("ড়", "ঢ়", "ড়", "ঢ়", altOptions = listOf("0"))
        )
        rows.add(KeyboardRow(row1))

        // Row 2 (A - L, ;)
        val row2 = listOf(
            KeyModel("ৃ", "ঋ", "ৃ", "ঋ", altOptions = listOf("অ", "আ")),
            KeyModel("ু", "ূ", "ু", "ূ", altOptions = listOf("উ", "ঊ")),
            KeyModel("ি", "ী", "ি", "ী", altOptions = listOf("ই", "ঈ")),
            KeyModel("া", "অ", "া", "অ", altOptions = listOf("আ")),
            KeyModel("্", "।", "্", "।", altOptions = listOf("হসন্ত")),
            KeyModel("ব", "ভ", "ব", "ভ"),
            KeyModel("ক", "খ", "ক", "খ"),
            KeyModel("ত", "থ", "ত", "থ"),
            KeyModel("দ", "ধ", "দ", "ধ"),
            KeyModel(";", ":", ";", ":")
        )
        rows.add(KeyboardRow(row2))

        // Row 3 (Shift, Z - M, Khanda-Ta, Dari, Backspace)
        val row3 = listOf(
            KeyModel(
                primaryLabel = "⇧",
                output = "",
                keyType = KeyType.SHIFT,
                weight = 1.25f
            ),
            KeyModel("্র", "্য", "\u09CD\u09B0", "\u09CD\u09AF"),
            KeyModel("ো", "ৌ", "ো", "ৌ", altOptions = listOf("ও", "ঔ")),
            KeyModel("ে", "ৈ", "ে", "ৈ", altOptions = listOf("এ", "ঐ")),
            KeyModel("র", "ল", "র", "ল", altOptions = listOf("র্")),
            KeyModel("ন", "ণ", "ন", "ণ"),
            KeyModel("স", "ষ", "স", "ষ"),
            KeyModel("ম", "শ", "ম", "শ"),
            KeyModel("ৎ", "ঁ", "ৎ", "ঁ"),
            KeyModel("।", "?", "।", "?"),
            KeyModel(
                primaryLabel = "⌫",
                output = "",
                keyType = KeyType.BACKSPACE,
                weight = 1.25f
            )
        )
        rows.add(KeyboardRow(row3))

        // Row 4 (Bottom / Space row)
        val row4 = listOf(
            KeyModel("?123", output = "", keyType = KeyType.MODE_SWITCH, weight = 1.2f),
            KeyModel("🌐", output = "", keyType = KeyType.LANGUAGE_SWITCH, weight = 1.0f),
            KeyModel(",", output = ",", keyType = KeyType.COMMA, weight = 0.9f),
            KeyModel("বাংলা (Bijoy)", output = " ", keyType = KeyType.SPACE, weight = 4.2f),
            KeyModel(".", output = "।", keyType = KeyType.PERIOD, weight = 0.9f),
            KeyModel("↵", output = "\n", keyType = KeyType.ENTER, weight = 1.5f)
        )
        rows.add(KeyboardRow(row4))

        return KeyboardLayoutData(rows)
    }

    fun getEnglishLayout(isShift: Boolean, showNumberRow: Boolean): KeyboardLayoutData {
        val rows = mutableListOf<KeyboardRow>()

        if (showNumberRow) {
            val numKeys = (1..9).map {
                KeyModel(it.toString(), altOptions = listOf(it.toString()))
            } + KeyModel("0", altOptions = listOf("0"))
            rows.add(KeyboardRow(numKeys))
        }

        val row1Letters = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
        rows.add(KeyboardRow(row1Letters.map {
            KeyModel(
                primaryLabel = if (isShift) it.uppercase() else it,
                shiftedLabel = it.uppercase(),
                output = if (isShift) it.uppercase() else it,
                shiftedOutput = it.uppercase()
            )
        }))

        val row2Letters = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
        rows.add(KeyboardRow(row2Letters.map {
            KeyModel(
                primaryLabel = if (isShift) it.uppercase() else it,
                shiftedLabel = it.uppercase(),
                output = if (isShift) it.uppercase() else it,
                shiftedOutput = it.uppercase()
            )
        }))

        val row3Keys = mutableListOf<KeyModel>()
        row3Keys.add(KeyModel("⇧", output = "", keyType = KeyType.SHIFT, weight = 1.3f))
        listOf("z", "x", "c", "v", "b", "n", "m").forEach {
            row3Keys.add(
                KeyModel(
                    primaryLabel = if (isShift) it.uppercase() else it,
                    shiftedLabel = it.uppercase(),
                    output = if (isShift) it.uppercase() else it,
                    shiftedOutput = it.uppercase()
                )
            )
        }
        row3Keys.add(KeyModel("⌫", output = "", keyType = KeyType.BACKSPACE, weight = 1.3f))
        rows.add(KeyboardRow(row3Keys))

        val row4 = listOf(
            KeyModel("?123", output = "", keyType = KeyType.MODE_SWITCH, weight = 1.2f),
            KeyModel("🌐", output = "", keyType = KeyType.LANGUAGE_SWITCH, weight = 1.0f),
            KeyModel(",", output = ",", keyType = KeyType.COMMA, weight = 0.9f),
            KeyModel("English", output = " ", keyType = KeyType.SPACE, weight = 4.2f),
            KeyModel(".", output = ".", keyType = KeyType.PERIOD, weight = 0.9f),
            KeyModel("↵", output = "\n", keyType = KeyType.ENTER, weight = 1.5f)
        )
        rows.add(KeyboardRow(row4))

        return KeyboardLayoutData(rows)
    }

    fun getSymbolsLayout(isPage2: Boolean): KeyboardLayoutData {
        val rows = mutableListOf<KeyboardRow>()

        if (!isPage2) {
            // Page 1: 1-0 and common punctuation
            val row0 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").map { KeyModel(it) }
            val row1 = listOf("@", "#", "৳", "$", "_", "&", "-", "+", "(", ")").map { KeyModel(it) }
            val row2 = listOf("*", "\"", "'", ":", ";", "!", "?", "\\", "/", "%").map { KeyModel(it) }

            val row3 = listOf(
                KeyModel("=\\<", output = "", keyType = KeyType.SHIFT, weight = 1.3f),
                KeyModel("~"), KeyModel("`"), KeyModel("|"), KeyModel("•"),
                KeyModel("√"), KeyModel("π"), KeyModel("÷"), KeyModel("×"),
                KeyModel("⌫", output = "", keyType = KeyType.BACKSPACE, weight = 1.3f)
            )

            val row4 = listOf(
                KeyModel("ABC", output = "", keyType = KeyType.MODE_SWITCH, weight = 1.3f),
                KeyModel("🌐", output = "", keyType = KeyType.LANGUAGE_SWITCH, weight = 1.0f),
                KeyModel(",", output = ",", weight = 0.9f),
                KeyModel("Space", output = " ", keyType = KeyType.SPACE, weight = 4.2f),
                KeyModel(".", output = ".", weight = 0.9f),
                KeyModel("↵", output = "\n", keyType = KeyType.ENTER, weight = 1.5f)
            )

            rows.addAll(listOf(KeyboardRow(row0), KeyboardRow(row1), KeyboardRow(row2), KeyboardRow(row3), KeyboardRow(row4)))
        } else {
            // Page 2: Advanced math & symbols
            val row0 = listOf("~", "`", "|", "•", "√", "π", "÷", "×", "¶", "∆").map { KeyModel(it) }
            val row1 = listOf("£", "€", "¥", "¢", "^", "°", "=", "{", "}", "\\").map { KeyModel(it) }
            val row2 = listOf("%", "©", "®", "™", "✓", "[", "]", "<", ">", "§").map { KeyModel(it) }

            val row3 = listOf(
                KeyModel("?123", output = "", keyType = KeyType.SHIFT, weight = 1.3f),
                KeyModel("«"), KeyModel("»"), KeyModel("¡"), KeyModel("¿"),
                KeyModel("—"), KeyModel("–"), KeyModel("…"), KeyModel("≠"),
                KeyModel("⌫", output = "", keyType = KeyType.BACKSPACE, weight = 1.3f)
            )

            val row4 = listOf(
                KeyModel("ABC", output = "", keyType = KeyType.MODE_SWITCH, weight = 1.3f),
                KeyModel("🌐", output = "", keyType = KeyType.LANGUAGE_SWITCH, weight = 1.0f),
                KeyModel(",", output = ",", weight = 0.9f),
                KeyModel("Space", output = " ", keyType = KeyType.SPACE, weight = 4.2f),
                KeyModel(".", output = ".", weight = 0.9f),
                KeyModel("↵", output = "\n", keyType = KeyType.ENTER, weight = 1.5f)
            )

            rows.addAll(listOf(KeyboardRow(row0), KeyboardRow(row1), KeyboardRow(row2), KeyboardRow(row3), KeyboardRow(row4)))
        }

        return KeyboardLayoutData(rows)
    }
}
