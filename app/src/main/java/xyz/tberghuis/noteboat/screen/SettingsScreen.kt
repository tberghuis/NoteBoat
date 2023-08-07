package xyz.tberghuis.noteboat.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import xyz.tberghuis.noteboat.R
import xyz.tberghuis.noteboat.utils.logd

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


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsContent(padding: PaddingValues) {

  val permissionState = rememberPermissionState(
    android.Manifest.permission.POST_NOTIFICATIONS
  )

  val notificationPermissionGranted = remember {
    derivedStateOf {
      // doesitblend yes
      (permissionState.status == PermissionStatus.Granted)
    }
  }

  // this is wrong, but its the users fault for not saying "allow"
  val launchPermissionRequest = remember {
    var count = 0
    {
      if (count < 1) {
        permissionState.launchPermissionRequest()
      } else {
        // todo send to systems settings (notifications)
        logd("send to systems settings (notifications)")
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
            // todo send to systems settings (notifications)
          }
          null
        }
      )
    }


  }

}