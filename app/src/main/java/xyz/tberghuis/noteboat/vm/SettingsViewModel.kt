package xyz.tberghuis.noteboat.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.tberghuis.noteboat.data.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val preferencesRepository: PreferencesRepository
) : ViewModel() {
  val showShortcutLockScreenFlow = preferencesRepository.showShortcutLockScreenFlow

  fun showShortcutClick() {
    viewModelScope.launch {
      preferencesRepository.toggleShowShortcutLockScreen()
    }
  }
}