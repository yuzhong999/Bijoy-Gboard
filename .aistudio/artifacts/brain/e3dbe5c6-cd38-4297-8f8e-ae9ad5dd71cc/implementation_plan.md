# Bijoy Gboard – Modern Bengali Android Keyboard

A production-grade Android Input Method Editor (IME) combining the authentic Bengali typing layout, vowel signs (কার), and conjunct consonants (যুক্তবর্ণ) of Bijoy Keyboard with the modern user experience, Material Design 3 aesthetics, and responsive performance of Google Gboard. Supports full English QWERTY, external physical USB/Bluetooth keyboards, customizable themes, and a companion settings application with an interactive typing sandbox.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The application will output standard **Bengali Unicode** (U+0980–U+09FF) so typed text works natively across all modern Android applications (WhatsApp, Chrome, Facebook, Telegram, Gmail, SMS, Notes) without requiring custom legacy fonts, while preserving 100% of the authentic Bijoy key positions, vowel modifier rules, and the `g` (Hasanta) conjunct-linking engine.

- **Bengali Output Encoding**: Standard Bengali Unicode (compatible with all modern Android text fields) with authentic Bijoy key positions and typing sequences.
- **Physical Keyboard Input**: Intercepts hardware USB/Bluetooth key events inside `InputMethodService` and runs them through the Bijoy mapping engine so external physical keyboards type Bijoy Bengali seamlessly.
- **Visual Design**: Google Gboard aesthetic featuring rounded rectangular keys, subtle elevation, key press popups, expandable toolbar (Settings, Themes, Clipboard, Emoji, Language Switcher), and M3 dynamic themes.
- **Zero Cloud / 100% Offline**: All character mapping, layouts, sound/haptic triggers, and settings operate entirely offline with zero network permissions or external data transmission.

---

## 1. Overview & Core Concept

### What It Does
- **System-wide Android IME**: Acts as a real, selectable Android on-screen keyboard configured through Android Settings (`Languages & input > On-screen keyboard`).
- **Dual Language Engine**:
  - **বাংলা (Bijoy Layout)**: Authentic key layout, vowel signs (কার: া, ি, ী, ু, ূ, ৃ, ে, ৈ, ো, ৌ), consonant modifiers (ফলা: য-ফলা, র-ফলা), conjunct consonants (যুক্তবর্ণ formed via `g` Hasanta linker), and Bengali numerals (১–০).
  - **English (QWERTY)**: Standard QWERTY layout with lowercase/uppercase, Shift/Caps Lock, numbers, and symbols.
- **Hardware Keyboard Support**: Detects connected USB and Bluetooth physical keyboards and maps hardware keystrokes to Bijoy Bengali or English according to the active mode.
- **Companion Settings App**: M3-designed application with keyboard activation wizard, theme picker, sound/vibration toggles, height adjustment, and an in-app test scratchpad.

---

## 2. Bijoy Layout & Character Mapping Specification

### A. Bengali Key Mapping Matrix (Unshifted & Shifted)

| Key | Default (Unshifted) | Shifted | Description & Role |
| :---: | :---: | :---: | :--- |
| **`Q`** | `ঙ` | `ং` | Consonant Nga / Anusvara modifier |
| **`W`** | `য` | `য়` | Antastha Ja / Antastha Ya |
| **`E`** | `ড` | `ঢ` | Retroflex Da / Dha |
| **`R`** | `প` | `ফ` | Labial Pa / Pha |
| **`T`** | `ট` | `ঠ` | Retroflex Ta / Tha |
| **`Y`** | `চ` | `ছ` | Palatal Ca / Cha |
| **`U`** | `জ` | `ঝ` | Palatal Ja / Jha |
| **`I`** | `হ` | `ঞ` | Glottal Ha / Palatal Nya |
| **`O`** | `গ` | `ঘ` | Velar Ga / Gha |
| **`P`** | `ড়` | `ঢ়` | Flapped Rra / Rrha |
| **`A`** | `ৃ` (Hri-kar) | `ঋ` (Vowel Hri) | Vowel sign & Independent vowel |
| **`S`** | `ু` (U-kar) | `ূ` (Uu-kar) | Short / Long U vowel signs |
| **`D`** | `ি` (I-kar) | `ী` (Ii-kar) | Short / Long I vowel signs |
| **`F`** | `া` (Aa-kar) | `অ` (Independent A) | Aa-kar & Primary Independent vowel |
| **`G`** | `্` (Hasanta / Virama) | `।` (Bengali Dari) | **Linker key** for Juktoborno & Full Vowels |
| **`H`** | `ব` | `ভ` | Labial Ba / Bha |
| **`J`** | `ক` | `খ` | Velar Ka / Kha |
| **`K`** | `ত` | `থ` | Dental Ta / Tha |
| **`L`** | `দ` | `ধ` | Dental Da / Dha |
| **`Z`** | `্র` (Ra-phala) | `্য` (Ya-phala) | Consonant sub-joins (ফলা) |
| **`X`** | `ো` (O-kar) | `ৌ` (Ou-kar) | O / Ou vowel signs |
| **`C`** | `ে` (E-kar) | `ৈ` (Oi-kar) | E / Oi vowel signs |
| **`V`** | `র` | `ল` | Liquid Ra / La |
| **`B`** | `ন` | `ণ` | Dental Na / Retroflex Nna |
| **`N`** | `স` | `ষ` | Dental Sa / Retroflex Ssa |
| **`M`** | `ম` | `শ` | Labial Ma / Palatal Sha |
| **`1` - `0`** | `১, ২, ৩, ৪, ৫, ৬, ৭, ৮, ৯, ০` | `!, @, #, ৳, %, ^, &, *, (, )` | Bengali Digits & Symbols (`$` -> `৳` Taka) |
| **`, . /`** | `,`, `.` / `।`, `/` | `ৎ` (Khanda-ta), `ঁ` (Chandrabindu), `?` | Punctuation & Modifiers |

### B. Vowel Sign (কার) and Independent Vowel Rules
In Bijoy, independent vowels are formed either via dedicated shifted keys or using the `g` (Linker) sequence:
- `অ` = `Shift + F`
- `আ` = `g` + `f` (or `Shift + F` + `f`)
- `ই` = `g` + `d`
- `ঈ` = `g` + `Shift + D`
- `উ` = `g` + `s`
- `ঊ` = `g` + `Shift + S`
- `ঋ` = `Shift + A`
- `এ` = `g` + `c`
- `ঐ` = `g` + `Shift + C`
- `ও` = `g` + `x`
- `ঔ` = `g` + `Shift + X`

### C. Conjunct Consonants (যুক্তবর্ণ) Engine
Conjunct consonants are formed by typing:
$$\text{Consonant 1} + \text{Linker } g \ (্) + \text{Consonant 2}$$

Examples handled by the `BijoyCharacterMapper`:
- `j` (ক) + `g` (্) + `j` (ক) $\rightarrow$ **ক্ক**
- `j` (ক) + `g` (্) + `Shift + N` (ষ) $\rightarrow$ **ক্ষ**
- `j` (ক) + `g` (্) + `k` (ত) $\rightarrow$ **ক্ত**
- `u` (জ) + `g` (্) + `Shift + I` (ঞ) $\rightarrow$ **জ্ঞ**
- `k` (ত) + `g` (্) + `k` (ত) $\rightarrow$ **ত্ত**
- `k` (ত) + `g` (্) + `h` (ব) $\rightarrow$ **ত্ব**
- `k` (ত) + `g` (্) + `m` (ম) $\rightarrow$ **ত্ম**
- `n` (স) + `g` (্) + `k` (ত) $\rightarrow$ **স্ত**
- `m` (শ) + `g` (্) + `v` (র) $\rightarrow$ **শ্র**
- `v` (র) + `g` (্) + Consonant $\rightarrow$ **রেফ (Ref)** (e.g. `v` + `g` + `m` $\rightarrow$ **র্ম**)

---

## 3. UI/UX & Gboard Visual Design

```
┌────────────────────────────────────────────────────────┐
│  [⚙️ Settings] [🎨 Themes] [📋 Clip] [😀 Emoji] [🌐 বাংলা/EN] │ <-- Toolbar
├────────────────────────────────────────────────────────┤
│   ১    ২    ৩    ৪    ৫    ৬    ৭    ৮    ৯    ০      │ <-- Row 1
│   ঙ    য    ড    প    ট    চ    জ    হ    গ    ড়      │ <-- Row 2
│   ৃ    ু    ি    া    ্    ব    ক    ত    দ    ;      │ <-- Row 3
│ [⇧]  ্র   ো   ে   র   ন   স   ম   ৎ   ।   [⌫]    │ <-- Row 4
│ [?123] [🌐] [    বাংলা (Bijoy)    ] [  .  ] [ ↵ Enter ] │ <-- Row 5
└────────────────────────────────────────────────────────┘
```

- **Keys Geometry & Materiality**:
  - Rounded rectangular keys (`corner radius: 6dp - 8dp`).
  - Subtle drop shadow / elevation on individual keycaps.
  - Light mode: Off-white canvas (`#F1F3F4`), Crisp white keycaps (`#FFFFFF`), Dark gray text (`#202124`), Tinted accent function keys (`#E8EAED`).
  - Dark mode: Charcoal background (`#1F1F1F`), Elevated dark gray keycaps (`#2D2E30`), White text (`#E8EAED`), Function key accent (`#3C4043`).
  - AMOLED Black mode: Pitch black background (`#000000`), Dark slate keycaps (`#18191B`).
  - Blue & Green themes: Material 3 dynamic color tones.
- **Interactive Feedback**:
  - **Key Popup Preview**: Floating rounded bubble displaying the enlarged character directly above the pressed key.
  - **Haptic Vibration**: Fine-grained haptic clicks via Android `Vibrator` with configurable duration (0–50ms).
  - **Audio Click**: Native typing click sounds via Android `AudioManager.FX_KEYPRESS_STANDARD`.
  - **Long-Press Secondary Popup**: Holding down keys displays alternate characters and symbols.
- **Gboard Toolbar Features**:
  - Quick language switch toggle (`বাংলা` / `English`).
  - Settings shortcut to launch customization screen directly.
  - Theme picker popup.
  - Clipboard history drawer.
  - Native emoji palette with category navigation.

---

## 4. Android Input Method Editor (IME) Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    BijoyKeyboardService                         │
│             (extends android.inputmethodservice.InputMethodService)
├───────────────────────────────┬─────────────────────────────────┤
│       Input Dispatcher        │        Physical Keyboard        │
│    • onKeyDown() / onKeyUp()  │        • Modifier state         │
│    • onStartInputView()       │        • Keycode translation    │
├───────────────────────────────┴─────────────────────────────────┤
│                       Layout & State Engine                     │
│    • Active Mode: BENGALI_BIJOY | ENGLISH_QWERTY | SYMBOLS      │
│    • Shift / CapsLock State                                     │
│    • Bengali Typing Engine (BijoyCharacterMapper & Ligatures)   │
├─────────────────────────────────────────────────────────────────┤
│                         UI Rendering                            │
│    • ComposeKeyboardView / Modern M3 Key Matrix                 │
│    • Dynamic Theme Manager & Live Layout Measurer               │
├─────────────────────────────────────────────────────────────────┤
│                       InputConnection                           │
│    • commitText() • deleteSurroundingText() • sendKeyEvent()    │
└─────────────────────────────────────────────────────────────────┘
```

### Component Breakdown
1. **`BijoyKeyboardService`**:
   - Registered in `AndroidManifest.xml` with `android.permission.BIND_INPUT_METHOD` and `<service>` meta-data linking to `method.xml`.
   - Manages keyboard lifecycle, IME window bounds, orientation changes, and communication with the focused `InputConnection`.
2. **`BengaliLayoutEngine` & `BijoyCharacterMapper`**:
   - Maintains Bijoy state machine (handling `g` linker sequences, standalone vowel synthesis, ref placement, and cursor movement).
   - Commits Unicode code points directly to `InputConnection`.
3. **`EnglishLayoutEngine`**:
   - Manages standard QWERTY lower/upper state, automatic capitalization at sentence starts, and symbol maps.
4. **`PhysicalKeyboardHandler`**:
   - Intercepts physical key events from external USB/Bluetooth keyboards (`KeyEvent.KEYCODE_*`).
   - If in Bengali mode, maps hardware keys (e.g. `KEYCODE_K` $\rightarrow$ `ত`, `KEYCODE_G` $\rightarrow$ `্`, etc.) and handles physical Shift/Alt keys without duplicate characters or OS conflicts.
5. **`KeyboardSettingsManager`**:
   - DataStore / SharedPreferences storage for themes, key vibration, sound, height scaling, number row toggle, and default language.
6. **`MainActivity` (Settings & Setup Wizard)**:
   - Full Material 3 setup interface:
     - Step 1: "Enable Bijoy Gboard" (opens `Settings.ACTION_INPUT_METHOD_SETTINGS`).
     - Step 2: "Select Input Method" (triggers `InputMethodManager.showInputMethodPicker()`).
     - Live interactive typing scratchpad to immediately test Bengali/English typing, key press sounds, and themes.
     - Customization controls (Themes, Key height, Haptic slider, Sound toggle).

---

## 5. Implementation Stages & Build Verification

1. **Stage 1: Manifest & IME Setup**:
   - Configure `AndroidManifest.xml` with `InputMethodService` and `res/xml/method.xml`.
   - Add app icon, strings, and permissions (`VIBRATE`).
2. **Stage 2: Core Character Engine & Keyboards**:
   - Implement `BijoyCharacterMapper` with complete Bengali Unicode mappings, Kar signs, Hasanta linker, and conjunct logic.
   - Implement `KeyboardLayout` models for Bengali Bijoy, English QWERTY, and Number/Symbol layouts.
3. **Stage 3: IME UI & Gboard Layout View**:
   - Create responsive keyboard view with rounded keycaps, toolbar, popups, and theme styling.
   - Implement key-press popup preview and touch handling.
4. **Stage 4: External Physical Keyboard Support**:
   - Implement `PhysicalKeyboardHandler` inside `BijoyKeyboardService` to process USB/Bluetooth hardware key events.
5. **Stage 5: Settings App & Interactive Scratchpad**:
   - Build Material 3 setup wizard in `MainActivity` with live typing test area and theme selector.
6. **Stage 6: Compilation & Verification**:
   - Verify zero compile errors via `compile_applet`.
   - Test orientation handling and language switching.
