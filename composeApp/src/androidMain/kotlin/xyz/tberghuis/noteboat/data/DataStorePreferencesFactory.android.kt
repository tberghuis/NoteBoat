package xyz.tberghuis.noteboat.data

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import xyz.tberghuis.noteboat.DATASTORE_FILENAME

actual class DataStorePreferencesFactory(
  private val app: Application
) {
  actual fun create(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
      produceFile = { app.filesDir.resolve("datastore/$DATASTORE_FILENAME").absolutePath.toPath() }
    )
  }
}