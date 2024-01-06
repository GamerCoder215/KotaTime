package me.gamercoder215.kotatime.util

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.lang.reflect.Field
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun smallSpacer() = Spacer(Modifier.size(14.dp))

@Composable
fun mediumSpacer() = Spacer(Modifier.size(28.dp))

// Extensions

val Field.wrapperType: Class<*>
    get() {
        if (!type.isPrimitive) return type

        return when (type) {
            Boolean::class.javaPrimitiveType -> Boolean::class.java
            Byte::class.javaPrimitiveType -> Byte::class.java
            Short::class.javaPrimitiveType -> Short::class.java
            Int::class.javaPrimitiveType -> Int::class.java
            Long::class.javaPrimitiveType -> Long::class.java
            Float::class.javaPrimitiveType -> Float::class.java
            Double::class.javaPrimitiveType -> Double::class.java
            Char::class.javaPrimitiveType -> Char::class.java
            Void::class.javaPrimitiveType -> Void::class.java
            else -> type
        }
    }

val String.asDate: Date
    get() = Date.from(Instant.from(
        DateTimeFormatter.ISO_INSTANT.parse(this)
    ))