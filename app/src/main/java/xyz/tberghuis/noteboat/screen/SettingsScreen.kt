package xyz.tberghuis.noteboat.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
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
  // todo
//    .putExtra(Settings.EXTRA_CHANNEL_ID, MY_CHANNEL_ID)
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

    Row {
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
          null
        }
      )
    }
    Row {
      LockscreenShortcutSetting()
    }


  }
}

@Composable
fun LockscreenShortcutSetting(vm: SettingsViewModel = hiltViewModel()) {

  val checked by vm.showShortcutLockScreenFlow.collectAsState(false)

  Text("Shortcut \"New voice note\" on lock screen")
  Switch(
    checked = checked,
    onCheckedChange = {
      vm.showShortcutClick()
      null
    },
  )
}

