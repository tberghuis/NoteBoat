package xyz.tberghuis.noteboat.tmp3

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import xyz.tberghuis.noteboat.screen.BackupDatabase
import xyz.tberghuis.noteboat.screen.LockscreenShortcutSetting
import xyz.tberghuis.noteboat.screen.SettingsScreenRow
import xyz.tberghuis.noteboat.screen.openAppNotificationSettings
import xyz.tberghuis.noteboat.utils.logd
import xyz.tberghuis.noteboat.vm.SettingsViewModel


@Composable
fun DeleteAllNotesButton(
  vm: SettingsViewModel = viewModel()
) {
  Button(onClick = {
    vm.confirmDeleteAllNotesDialog = true
  }) {
    Text("Delete All Notes")
  }
  if (vm.confirmDeleteAllNotesDialog) {
    AlertDialog(
      onDismissRequest = { vm.confirmDeleteAllNotesDialog = false },
      confirmButton = {
        Button(onClick = {
          vm.confirmDeleteAllNotesDialog = false
          vm.deleteAllNotes()
        }) {
          Text("OK")
        }
      },
      modifier = Modifier,
      dismissButton = {
        Button(onClick = {
          vm.confirmDeleteAllNotesDialog = false
        }) {
          Text("Cancel")
        }
      },
      text = {
        Text("Confirm delete all notes?")
      },
    )
  }
}

@Composable
fun ImportFromBackupButton(
  vm: SettingsViewModel = viewModel()
) {
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      logd("rememberLauncherForActivityResult $result")
      when (result.resultCode) {
        RESULT_OK -> {
          result.data?.data?.let { vm.importDb(it) }
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