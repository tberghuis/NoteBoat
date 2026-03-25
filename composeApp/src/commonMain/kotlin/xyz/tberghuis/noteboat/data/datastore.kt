package xyz.tberghuis.noteboat.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
  PreferenceDataStoreFactory.createWithPath(
    produceFile = { producePath().toPath() }
  )

// todo use same filename as old noteboat???
internal const val dataStoreFileName = "tmp.preferences_pb"
