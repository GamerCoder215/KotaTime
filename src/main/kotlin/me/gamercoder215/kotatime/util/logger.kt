package me.gamercoder215.kotatime.util

import me.gamercoder215.kotatime.storage.StorageManager.folder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val LOG_FOLDER = File(folder, "logs")
val LOG_FILE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
val LOG_TIME_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

val logger: Logger = LoggerFactory.getLogger("KotaTime")

fun currentLogFile(): File {
    if (!LOG_FOLDER.exists()) LOG_FOLDER.mkdirs()

    val date = Date()
    val file = File(LOG_FOLDER, "${LOG_FILE_FORMAT.format(date)}.log")
    if (!file.exists()) file.createNewFile()
    return file
}

fun info(msg: String) {
    logger.info(msg)
    currentLogFile().appendText("[${LOG_TIME_FORMAT.format(Date())}] [INFO] $msg\n")
}

fun warn(msg: String) {
    logger.warn(msg)
    currentLogFile().appendText("[${LOG_TIME_FORMAT.format(Date())}] [WARN] $msg\n")
}

fun severe(msg: String) {
    logger.error(msg)
    currentLogFile().appendText("[${LOG_TIME_FORMAT.format(Date())}] [ERROR] $msg\n")
}

fun debug(msg: String) {
    logger.debug(msg)
    currentLogFile().appendText("[${LOG_TIME_FORMAT.format(Date())}] [DEBUG] $msg\n")
}