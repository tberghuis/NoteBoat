package xyz.tberghuis.noteboat.tmp

import android.app.Application
import android.content.Context
import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.LegacyDatabase
import xyz.tberghuis.noteboat.data.Note
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.utils.Log
import javax.inject.Inject

@Composable
fun MigrationDemo() {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val viewModel: MigrationViewModel = hiltViewModel()

  Column {
    Text("hello migration")
    Button(onClick = {
      Log.d("xxx", "migrrate click")
      scope.launch {
        viewModel.migrate()
      }


    }) {
      Text("migrate")
    }
  }
}

@HiltViewModel
class MigrationViewModel @Inject constructor(
  @ApplicationContext val appContext: Context,
  private val noteDao: NoteDao,
) : ViewModel() {

  suspend fun migrate() {
    // "/data/user/0/site.thomasberghuis.noteboat/app_flutter/notes.db"
    val path = "${appContext.filesDir.path}/../app_flutter/notes.db"

    val legacyDb = Room.databaseBuilder(
      appContext,
      LegacyDatabase::class.java,
      path
    ).build()
    Log.d("xxx", "legacyDb $legacyDb")

    withContext(Dispatchers.IO) {
      val legacyNotes = legacyDb.legacyNoteDao().getAll()
      Log.d("xxx", "legacyNotes $legacyNotes")

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

  }

}
