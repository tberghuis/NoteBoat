package xyz.tberghuis.noteboat.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.tberghuis.noteboat.data.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val preferencesRepository: PreferencesRepository
) : ViewModel() {
val fsdf="fdsfsdf"
}

