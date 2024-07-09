package xyz.tberghuis.noteboat.tmp3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TmpSettingsContent(padding: PaddingValues) {
  val context = LocalContext.current
  val permissionState = rememberPermissionState(
    android.Manifest.permission.POST_NOTIFICATIONS
  )

  val notificationPermissionGranted = remember {
    derivedStateOf {
      // doesitblend yes
      (permissionState.status == PermissionStatus.Granted)
    }
  }

  // this is wrong, but its the users fault for not saying "allow" first time
  val launchPermissionRequest = remember {
    var count = 0
    {
      if (count < 1) {
        permissionState.launchPermissionRequest()
      } else {
        openAppNotificationSettings(context)
      }
      count++
    }
  }

  Column(
    modifier = Modifier
      .padding(padding)
      .verticalScroll(rememberScrollState())
      .fillMaxSize()

  ) {
    SettingsScreenRow {
      Text("Notification permission")
      Switch(
        checked = notificationPermissionGranted.value,
        onCheckedChange = {
          logd("onCheckedChange $it")
          if (it) {
            launchPermissionRequest.invoke()
          } else {
            openAppNotificationSettings(context)
          }
        }
      )
    }
    SettingsScreenRow {
      LockscreenShortcutSetting()
    }
    SettingsScreenRow(
      horizontalArrangement = Arrangement.Center,
    ) {
      BackupDatabase()
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

@Composable
fun DeleteAllNotesButton(
  vm: SettingsViewModel = viewModel()
) {
  Button(onClick = {
  }) {
    Text("Delete All Notes")
  }


  AlertDialog(
    onDismissRequest = {},
    confirmButton = {},
    modifier = Modifier,
    dismissButton = {},
    text = {
      Text("Confirm delete all notes?")
    },
  )


}

@Composable
fun ImportFromBackupButton(
  vm: SettingsViewModel = viewModel()
) {
  Button(onClick = {
  }) {
    Text("Import Notes from Backup")
  }
}

