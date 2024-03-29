package me.gamercoder215.kotatime.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.kanro.compose.jetbrains.expui.control.Label
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButtonColors
import io.kanro.compose.jetbrains.expui.theme.DarkTheme
import io.kanro.compose.jetbrains.expui.theme.LightTheme
import me.gamercoder215.kotatime.recompose
import me.gamercoder215.kotatime.storage.StorageManager.saveSettings
import me.gamercoder215.kotatime.storage.StorageManager.settings
import me.gamercoder215.kotatime.util.isSupported

var darkMode: Boolean
    get() {
        return settings["dark_mode"].toBoolean()
    }
    set(value) {
        settings["dark_mode"] = value.toString()
        saveSettings()
        recompose()
    }

var lang: String
    get() {
        val lang = settings["lang"]
        if (lang == null) {
            val lang0 = System.getProperty("user.language")
            if (isSupported(lang0)) {
                settings["lang"] = lang0
                saveSettings()
                return lang0
            } else {
                settings["lang"] = "en"
                saveSettings()
                return "en"
            }
        }

        return settings["lang"] ?: System.getProperty("user.language")
    }
    set(value) {
        settings["lang"] = value
        saveSettings()
        recompose()
    }

val font = FontFamily(
    Font(
        "assets/fonts/Switzer-Bold.otf",
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    ),
    Font(
        "assets/fonts/Switzer-BoldItalic.otf",
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),

    Font(
        "assets/fonts/Switzer-Medium.otf",
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    ),
    Font(
        "assets/fonts/Switzer-MediumItalic.otf",
        weight = FontWeight.Medium,
        style = FontStyle.Italic
    ),

    Font(
        "assets/fonts/Switzer-Regular.otf",
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        "assets/fonts/Switzer-RegularItalic.otf",
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),

    Font(
        "assets/fonts/Switzer-Light.otf",
        weight = FontWeight.Light,
        style = FontStyle.Normal
    ),
    Font(
        "assets/fonts/Switzer-LightItalic.otf",
        weight = FontWeight.Light,
        style = FontStyle.Italic
    ),
) as FontListFontFamily

// Sizes

const val TEXT_SIZE = 11
const val H1_TEXT_SIZE = 36
const val H2_TEXT_SIZE = 24
const val H3_TEXT_SIZE = 18
const val H4_TEXT_SIZE = 8

// Colors

const val DARK_TEXT = 0xFEFEFE

const val DARK_BACKGROUND = 0x222222
const val DARK_BACKGROUND_2 = 0x343434
const val DARK_BACKGROUND_3 = 0x424242


const val LIGHT_TEXT = 0x010101

const val LIGHT_BACKGROUND = 0xF0F0F0
const val LIGHT_BACKGROUND_2 = 0xC1C1C1
const val LIGHT_BACKGROUND_3 = 0xB2B2B2

// Colors - UI Components

val TOOLBAR_BUTTON_COLORS: ToolBarActionButtonColors
    get() = if (darkMode)
            DarkTheme.ToolBarActionButtonColors
        else
            LightTheme.ToolBarActionButtonColors

// Functions

@Composable
fun Text(text: String, fontSize: TextUnit = TEXT_SIZE.sp, modifier: Modifier = Modifier, style: TextStyle = TextStyle.Default) {
    Label(text,
        modifier = modifier,
        color = if (darkMode) Color(DARK_TEXT.alpha) else Color(LIGHT_TEXT.alpha),
        fontSize = fontSize,
        fontFamily = font,
        style = style
    )
}

@Composable
fun H1(text: String, modifier: Modifier = Modifier, style: TextStyle = TextStyle.Default) = Text(text, H1_TEXT_SIZE.sp, modifier, style)

@Composable
fun H2(text: String, modifier: Modifier = Modifier, style: TextStyle = TextStyle.Default) = Text(text, H2_TEXT_SIZE.sp, modifier, style)

@Composable
fun H3(text: String, modifier: Modifier = Modifier, style: TextStyle = TextStyle.Default) = Text(text, H3_TEXT_SIZE.sp, modifier, style)

@Composable
fun H4(text: String, modifier: Modifier = Modifier, style: TextStyle = TextStyle.Default) = Text(text, H4_TEXT_SIZE.sp, modifier, style)