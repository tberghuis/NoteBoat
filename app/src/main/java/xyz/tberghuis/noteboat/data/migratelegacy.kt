package xyz.tberghuis.noteboat.data

import android.app.Application
import androidx.room.Room
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.io.File

suspend fun migrateLegacy(application: Application, optionDao: OptionDao, noteDao: NoteDao) {
  val runMigration = optionDao.getOption("run_legacy_migration")
  if (runMigration != "true") {
    return
  }
  // eagerly update??? probably bad to assume migration will complete, DOITWRONG
  optionDao.updateOption("run_legacy_migration", "false")
  val file = File(application.filesDir, "../app_flutter/notes.db")
  if (!file.exists()) {
    return
  }
  val legacyDb = Room.databaseBuilder(
    application, LegacyDatabase::class.java, file.path
  ).build()
  val legacyNotes = legacyDb.legacyNoteDao().getAll()
  legacyNotes.forEach {
    val noteText = it.noteText!!
    fun toEpoch(s: String?): Long {
      return s!!.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()
    }
    val createdEpoch = toEpoch(it.createdDate)
    val modifiedEpoch = toEpoch(it.modifiedDate)
    val note =
      Note(noteText = noteText, createdEpoch = createdEpoch, modifiedEpoch = modifiedEpoch)
    noteDao.insertAll(note)
  }
}