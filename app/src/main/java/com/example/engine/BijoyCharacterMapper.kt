package com.example.engine

import android.view.inputmethod.InputConnection

object BijoyCharacterMapper {

    private val unshiftedMap = mapOf(
        '1' to "১",
        '2' to "২",
        '3' to "৩",
        '4' to "৪",
        '5' to "৫",
        '6' to "৬",
        '7' to "৭",
        '8' to "৮",
        '9' to "৯",
        '0' to "০",

        'q' to "ঙ",
        'w' to "য",
        'e' to "ড",
        'r' to "প",
        't' to "ট",
        'y' to "চ",
        'u' to "জ",
        'i' to "হ",
        'o' to "গ",
        'p' to "ড়",

        'a' to "ৃ",
        's' to "ু",
        'd' to "ি",
        'f' to "া",
        'g' to "্",
        'h' to "ব",
        'j' to "ক",
        'k' to "ত",
        'l' to "দ",
        ';' to ";",
        '\'' to "'",

        'z' to "\u09CD\u09B0", // ্র Ra-phala
        'x' to "ো",
        'c' to "ে",
        'v' to "র",
        'b' to "ন",
        'n' to "স",
        'm' to "ম",
        ',' to ",",
        '.' to "।",
        '/' to "/"
    )

    private val shiftedMap = mapOf(
        '1' to "!",
        '2' to "@",
        '3' to "#",
        '4' to "৳",
        '5' to "%",
        '6' to "ঃ",
        '7' to "&",
        '8' to "*",
        '9' to "(",
        '0' to ")",

        'q' to "ং",
        'w' to "য়",
        'e' to "ঢ",
        'r' to "ফ",
        't' to "ঠ",
        'y' to "ছ",
        'u' to "ঝ",
        'i' to "ঞ",
        'o' to "ঘ",
        'p' to "ঢ়",

        'a' to "ঋ",
        's' to "ূ",
        'd' to "ী",
        'f' to "অ",
        'g' to "।",
        'h' to "ভ",
        'j' to "খ",
        'k' to "থ",
        'l' to "ধ",
        ';' to ":",
        '\'' to "\"",

        'z' to "\u09CD\u09AF", // ্য Ya-phala
        'x' to "ৌ",
        'c' to "ৈ",
        'v' to "ল",
        'b' to "ণ",
        'n' to "ষ",
        'm' to "শ",
        ',' to "ৎ",
        '.' to "ঁ",
        '/' to "?"
    )

    // Independent vowel synthesis when typed after 'g' (্)
    private val linkerVowelMap = mapOf(
        "া" to "আ",
        "ি" to "ই",
        "ী" to "ঈ",
        "ু" to "উ",
        "ূ" to "ঊ",
        "ৃ" to "ঋ",
        "ে" to "এ",
        "ৈ" to "ঐ",
        "ো" to "ও",
        "ৌ" to "ঔ",
        "অ" to "আ"
    )

    fun getOutput(char: Char, isShift: Boolean): String? {
        val lower = char.lowercaseChar()
        return if (isShift) {
            shiftedMap[lower]
        } else {
            unshiftedMap[lower]
        }
    }

    /**
     * Commits a character in Bengali Bijoy mode into the InputConnection,
     * handling independent vowel synthesis via 'g' linker sequence and conjuncts.
     */
    fun commitBijoyInput(output: String, ic: InputConnection?) {
        if (ic == null || output.isEmpty()) return

        val textBefore = ic.getTextBeforeCursor(1, 0)?.toString() ?: ""

        // Case 1: If previous character is Hasanta ('্') and current character is a vowel sign (Kar),
        // replace Hasanta with independent vowel (e.g. g + f -> আ, g + d -> ই)
        if (textBefore == "্" && linkerVowelMap.containsKey(output)) {
            val independentVowel = linkerVowelMap[output] ?: output
            ic.deleteSurroundingText(1, 0)
            ic.commitText(independentVowel, 1)
            return
        }

        // Case 2: If previous character is 'অ' and current character is 'া', convert to 'আ'
        if (textBefore == "অ" && output == "া") {
            ic.deleteSurroundingText(1, 0)
            ic.commitText("আ", 1)
            return
        }

        // Default: commit character directly
        ic.commitText(output, 1)
    }

    /**
     * Maps hardware keycode char to Bijoy output and commits it.
     */
    fun processHardwareKey(char: Char, isShift: Boolean, ic: InputConnection?): Boolean {
        val output = getOutput(char, isShift) ?: return false
        commitBijoyInput(output, ic)
        return true
    }
}
