package xyz.tberghuis.noteboat.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

// need better factory name
expect class DataStorePreferencesFactory {
  fun create(): DataStore<Preferences>
}

//fun DataStorePreferencesFactory.createDataStore(producePath: () -> String): DataStore<Preferences> =
//  PreferenceDataStoreFactory.createWithPath(
//    produceFile = { producePath().toPath() }
//  )