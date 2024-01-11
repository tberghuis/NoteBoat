package xyz.tberghuis.noteboat.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.MainApplication

class SettingsViewModel(
  application: Application
) : AndroidViewModel(application) {
  private val preferencesRepository = (application as MainApplication).preferencesRepository

  val showShortcutLockScreenFlow = preferencesRepository.showShortcutLockScreenFlow

  fun showShortcutClick() {
    viewModelScope.launch {
      preferencesRepository.toggleShowShortcutLockScreen()
    }
  }
}