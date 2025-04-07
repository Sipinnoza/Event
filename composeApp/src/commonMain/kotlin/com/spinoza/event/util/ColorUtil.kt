package com.spinoza.event.util

import androidx.compose.ui.graphics.Color

object ColorUtil {
    fun parseColor(colorString: String): Color {
        return try {
            if (!colorString.startsWith("#")) {
                Color.White
            } else {
                val hex = colorString.removePrefix("#")
                val colorLong = when (hex.length) {
                    6 -> ("FF$hex")
                    8 -> hex
                    else -> return Color.White
                }.toLong(16)
                val a = ((colorLong shr 24) and 0xFF) / 255f
                val r = ((colorLong shr 16) and 0xFF) / 255f
                val g = ((colorLong shr 8) and 0xFF) / 255f
                val b = (colorLong and 0xFF) / 255f
                Color(red = r, green = g, blue = b, alpha = a)
            }
        } catch (e: Exception) {
            Color.White
        }
    }
}
