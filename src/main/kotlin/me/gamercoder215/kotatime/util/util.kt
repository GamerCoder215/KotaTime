package me.gamercoder215.kotatime.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.*
import java.io.*
import java.lang.reflect.Field
import java.net.URL
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.lang.reflect.Modifier as ReflectionModifier

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

fun <T> T.load(json: JsonObject): T {
    for (field in this!!::class.java.declaredFields)
        field.load(this, json[field.name] as? JsonPrimitive ?: continue)

    return this
}

fun Field.load(instance: Any?, value: JsonElement) {
    if (value !is JsonPrimitive) return

    isAccessible = true

    when (wrapperType) {
        Int::class.java -> this[instance] = value.intOrNull ?: return
        Double::class.java -> this[instance] = value.doubleOrNull ?: return
        String::class.java -> this[instance] = value.content
        Boolean::class.java -> this[instance] = value.booleanOrNull ?: return
    }
}

fun <T> File.serialize(obj: T) {
    val oos = ObjectOutputStream(BufferedOutputStream(outputStream()))
    oos.writeObject(obj)
    oos.close()
}

@Suppress("unchecked_cast")
fun <T> File.deserialize(): T? {
    val ois = ObjectInputStream(BufferedInputStream(inputStream()))
    val obj = ois.readObject()
    ois.close()
    return obj as? T
}

val String.asData: ByteArray
    get() {
        return if (startsWith("https://"))
            URL(this).readBytes()
        else asFileData
    }

val String.asFileData: ByteArray
    get() = File(this).readBytes()

fun Any.toJson(): JsonObject = buildJsonObject {
    for (field in this::class.java.declaredFields.filter { !ReflectionModifier.isFinal(it.modifiers) }.onEach { it.isAccessible = true }) {
        val value = field[null]
        if (value == null) {
            put(field.name, JsonNull)
            continue
        }

        when (field.wrapperType) {
            String::class.java -> put(field.name, value as? String)
            Int::class.java -> put(field.name, value as Int)
            Double::class.java -> put(field.name, value as Double)
            Boolean::class.java -> put(field.name, value as Boolean)
            else -> throw IllegalStateException("Unknown type: '${field.wrapperType}' for field '${field.name}'")
        }
    }
}

fun Any.fromJson(obj: JsonObject) {
    for (field in this::class.java.declaredFields.filter { !ReflectionModifier.isFinal(it.modifiers) }) {
        val value = obj[field.name] ?: continue
        if (value is JsonNull) continue

        field.load(this, value)
    }
}

fun Any.clearFields() {
    for (field in this::class.java.declaredFields.filter { !ReflectionModifier.isFinal(it.modifiers) }.onEach { it.isAccessible = true })
        field[this] = when (field.wrapperType) {
            String::class.java -> ""
            Int::class.java -> 0
            Double::class.java -> 0.0
            Boolean::class.java -> false
            else -> throw IllegalStateException("Unknown type: '${field.wrapperType}' for field '${field.name}'")
        }
}