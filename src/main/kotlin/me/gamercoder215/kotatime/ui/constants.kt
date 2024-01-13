package me.gamercoder215.kotatime.ui

import androidx.compose.ui.graphics.Color
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButtonColors
import io.kanro.compose.jetbrains.expui.style.AreaColors
import io.kanro.compose.jetbrains.expui.theme.DarkTheme
import io.kanro.compose.jetbrains.expui.theme.LightTheme
import me.gamercoder215.kotatime.recompose
import me.gamercoder215.kotatime.storage.StorageManager.saveSettings
import me.gamercoder215.kotatime.storage.StorageManager.settings

var darkMode: Boolean
    get() {
        return settings["dark_mode"].toBoolean()
    }
    set(value) {
        settings["dark_mode"] = value.toString()
        saveSettings()
        recompose()
    }

// Colors

const val DARK_TEXT = 0xFEFEFE

const val DARK_BACKGROUND = 0x222222
const val DARK_BACKGROUND_2 = 0x3A3A3A
const val DARK_BACKGROUND_3 = 0x424242


const val LIGHT_TEXT = 0x010101

const val LIGHT_BACKGROUND = 0xF0F0F0
const val LIGHT_BACKGROUND_2 = 0xD1D1D1
const val LIGHT_BACKGROUND_3 = 0xB3B3B3

// Colors - UI Components

val TOOLBAR_BUTTON_COLORS: ToolBarActionButtonColors
    get() = if (darkMode)
            DarkTheme.ToolBarActionButtonColors
        else
            LightTheme.ToolBarActionButtonColors
