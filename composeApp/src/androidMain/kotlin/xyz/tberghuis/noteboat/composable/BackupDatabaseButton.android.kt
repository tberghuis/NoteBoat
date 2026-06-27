package xyz.tberghuis.noteboat.composable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.noteboat.DEFAULT_BACKUP_DB_FILENAME
import xyz.tberghuis.noteboat.vm.SettingsViewModel

@Composable
actual fun BackupDatabaseButton() {
  val vm: SettingsViewModel = koinViewModel()
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/x-sqlite3")) { uri ->
      uri?.let {
        vm.backupDb(uri.toString())
      }
    }
  Button(onClick = {
    launcher.launch(DEFAULT_BACKUP_DB_FILENAME)
  }) {
    Text("Backup Database")
  }
}