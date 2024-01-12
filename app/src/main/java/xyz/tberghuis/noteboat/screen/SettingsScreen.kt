package xyz.tberghuis.noteboat.screen

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import xyz.tberghuis.noteboat.DEFAULT_BACKUP_DB_FILENAME
import xyz.tberghuis.noteboat.LOCK_SCREEN_CHANNEL_ID
import xyz.tberghuis.noteboat.R
import xyz.tberghuis.noteboat.utils.logd
import xyz.tberghuis.noteboat.vm.SettingsViewModel

@Composable
fun SettingsScreen(
  navController: NavHostController
) {

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier
          .statusBarsPadding(),
        title = { Text(stringResource(R.string.settings)) },
        navigationIcon = {

          IconButton(onClick = { navController.navigateUp() }) {
            Icon(
              imageVector = Icons.Filled.ArrowBack,
              contentDescription = stringResource(R.string.back)
            )
          }

        },
      )
    },
  ) { padding ->
    SettingsContent(padding)
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
fun SettingsContent(padding: PaddingValues) {
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
  }
}

@Composable
fun LockscreenShortcutSetting(vm: SettingsViewModel = viewModel()) {
  val checked by vm.showShortcutLockScreenFlow.collectAsState(false)
  Text("Shortcut \"New voice note\" on lock screen")
  Switch(
    checked = checked,
    onCheckedChange = {
      vm.showShortcutClick()
    },
  )
}

@Composable
fun SettingsScreenRow(
  horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
  content: @Composable RowScope.() -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(15.dp),
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    content()
  }
}

@Composable
fun BackupDatabase(
  vm: SettingsViewModel = viewModel()
) {
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