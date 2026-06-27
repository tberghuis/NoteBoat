package xyz.tberghuis.noteboat.composable

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import org.koin.compose.viewmodel.koinViewModel
import xyz.tberghuis.noteboat.vm.SettingsViewModel

@Composable
actual fun BackupDatabaseButton() {
  val vm: SettingsViewModel = koinViewModel()

  val launcher = rememberFileSaverLauncher(
    dialogSettings = FileKitDialogSettings.createDefault()
  ) { file ->
    vm.backupDb(file.toString())
  }
  Button(onClick = {
    launcher.launch(
      suggestedName = "noteboat-backup",
      defaultExtension = "db",
      allowedExtensions = setOf("db"),
    )
  }) {
    Text("Backup Database")
  }


}