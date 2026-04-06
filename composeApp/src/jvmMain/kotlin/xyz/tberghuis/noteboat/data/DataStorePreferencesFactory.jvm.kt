package xyz.tberghuis.noteboat.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File
import okio.Path.Companion.toPath
import xyz.tberghuis.noteboat.DATASTORE_FILENAME

actual class DataStorePreferencesFactory {
  actual fun create(): DataStore<Preferences> {
    val file = File(System.getProperty("java.io.tmpdir"), DATASTORE_FILENAME)
    return PreferenceDataStoreFactory.createWithPath(
      produceFile = { file.absolutePath.toPath() }
    )
  }
}