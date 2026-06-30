package xyz.tberghuis.noteboat.composable

import androidx.compose.runtime.Composable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@Composable
expect fun BackupDatabaseButton()

//fun backupDbFilename(): String {
//  return createMultiplatformFilename("noteboat_", "db")
//}
//
//private fun createMultiplatformFilename(baseName: String, extension: String): String {
//  val currentMoment = kotlin.time.Clock.System.now()
//  val localDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
//
//  val year = localDateTime.year
//  val month = localDateTime.month.number.toString().padStart(2, '0')
//  val day = localDateTime.day.toString().padStart(2, '0')
//  val hour = localDateTime.hour.toString().padStart(2, '0')
//  val minute = localDateTime.minute.toString().padStart(2, '0')
//  val second = localDateTime.second.toString().padStart(2, '0')
//
//  val timestamp = "${year}${month}${day}_${hour}${minute}${second}"
//  return "${baseName}_$timestamp.$extension"
//}

fun timestampNowString(): String {
  val currentMoment = kotlin.time.Clock.System.now()
  val localDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

  val year = localDateTime.year
  val month = localDateTime.month.number.toString().padStart(2, '0')
  val day = localDateTime.day.toString().padStart(2, '0')
  val hour = localDateTime.hour.toString().padStart(2, '0')
  val minute = localDateTime.minute.toString().padStart(2, '0')
  val second = localDateTime.second.toString().padStart(2, '0')

  return "${year}${month}${day}_${hour}${minute}${second}"
}
