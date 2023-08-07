package xyz.tberghuis.noteboat.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(
  name = "user_preferences",
)

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
  val showShortcutLockScreenFlow: Flow<Boolean> = dataStore.data.map { preferences ->
    preferences[booleanPreferencesKey("show_shortcut_lock_screen")] ?: false
  }

//  suspend fun updateShowShortcutLockScreen(showShortcutLockScreen: Boolean) {
//    dataStore.edit { preferences ->
//      preferences[booleanPreferencesKey("show_shortcut_lock_screen")] = showShortcutLockScreen
//    }
//  }

  suspend fun toggleShowShortcutLockScreen() {
    dataStore.edit { preferences ->
      val currentValue = preferences[booleanPreferencesKey("show_shortcut_lock_screen")] ?: false
      preferences[booleanPreferencesKey("show_shortcut_lock_screen")] = !currentValue
    }
  }
}