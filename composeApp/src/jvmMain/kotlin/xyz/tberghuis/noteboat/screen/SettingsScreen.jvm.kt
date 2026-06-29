package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.tberghuis.noteboat.composable.BackupDatabaseButton
import xyz.tberghuis.noteboat.composable.DeleteAllNotesButton
import xyz.tberghuis.noteboat.composable.ImportFromBackupButton

@Composable
actual fun SettingsContent(padding: PaddingValues) {


  Column(
    modifier = Modifier
      .padding(padding)
      .verticalScroll(rememberScrollState())
      .fillMaxSize()
  ) {

    SettingsScreenRow(
      horizontalArrangement = Arrangement.Center,
    ) {
      BackupDatabaseButton()
    }
    SettingsScreenRow(
      horizontalArrangement = Arrangement.Center,
    ) {
      DeleteAllNotesButton()
    }
    SettingsScreenRow(
      horizontalArrangement = Arrangement.Center,
    ) {
      ImportFromBackupButton()
    }
  }
}