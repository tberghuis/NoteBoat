package xyz.tberghuis.noteboat.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import xyz.tberghuis.noteboat.DEFAULT_BACKUP_DB_FILENAME
import xyz.tberghuis.noteboat.utils.logd
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import xyz.tberghuis.noteboat.LOCK_SCREEN_CHANNEL_ID
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.noteboat.vm.TmpSettingsViewModel


@Composable
fun BackupDatabase() {
  val vm: TmpSettingsViewModel = koinViewModel()
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/x-sqlite3")) { uri ->
      uri?.let {
        vm.backupDb(uri)
      }
    }
  Button(onClick = {
    launcher.launch(DEFAULT_BACKUP_DB_FILENAME)
  }) {
    Text("Backup Database")
  }
}

@Composable
fun ImportFromBackupButton() {
  val vm: TmpSettingsViewModel = koinViewModel()
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

fun openAppNotificationSettings(context: Context) {
  val intent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    .putExtra(Settings.EXTRA_CHANNEL_ID, LOCK_SCREEN_CHANNEL_ID)
  context.startActivity(intent)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun SettingsContent(padding: PaddingValues) {
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
  vm: TmpSettingsViewModel = koinViewModel()
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
fun LockscreenShortcutSetting(vm: TmpSettingsViewModel = koinViewModel()) {
  val checked by vm.showShortcutLockScreenFlow.collectAsState(false)
  Text("Shortcut \"New voice note\" on lock screen")
  Switch(
    checked = checked,
    onCheckedChange = {
      vm.showShortcutClick()
    },
  )
}


