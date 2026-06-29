package xyz.tberghuis.noteboat.composable

import androidx.compose.runtime.Composable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import xyz.tberghuis.noteboat.utils.logd
import android.app.Activity.RESULT_OK
import android.content.Intent
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.noteboat.vm.SettingsViewModel

@Composable
actual fun ImportFromBackupButton() {
  val vm: SettingsViewModel = koinViewModel()
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      logd("rememberLauncherForActivityResult $result")
      when (result.resultCode) {
        RESULT_OK -> {
          result.data?.data?.let { vm.importDb(it.toString()) }
        }
      }
    }

  Button(onClick = {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
      type = "*/*"
      addCategory(Intent.CATEGORY_OPENABLE)
    }
    launcher.launch(intent)
  }) {
    Text("Import Notes from Backup")
  }
}
