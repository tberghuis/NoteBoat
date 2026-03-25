package xyz.tberghuis.noteboat.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createDataStoreJvm(): DataStore<Preferences> = createDataStore(
  producePath = {
    val file = File(System.getProperty("java.io.tmpdir"), dataStoreFileName)
    file.absolutePath
  }
)