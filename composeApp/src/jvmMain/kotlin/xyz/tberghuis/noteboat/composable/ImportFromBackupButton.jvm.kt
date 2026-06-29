package xyz.tberghuis.noteboat.composable

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.viewmodel.koinViewModel
import xyz.tberghuis.noteboat.vm.SettingsViewModel

@Composable
actual fun ImportFromBackupButton() {
  val vm: SettingsViewModel = koinViewModel()
  val launcher = rememberFilePickerLauncher { file ->
    println("rememberFilePickerLauncher file $file")
    vm.importDb(file.toString())
  }
  Button(onClick = {
    launcher.launch()
  }) {
    Text("Import Notes from Backup")
  }
}