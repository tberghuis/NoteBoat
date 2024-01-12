package xyz.tberghuis.noteboat.tmp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.utils.logd

@Composable
fun TmpScreen(
  vm: TmpVm = viewModel()
) {
  Column {
    Text("hello tmp screen")
    Button(onClick = {
      vm.checkpoint()
    }) {
      Text("checkpoint")
    }
    CreateDbBackup()
    WriteDbFile()
  }
}

@Composable
fun CreateDbBackup(
  vm: TmpVm = viewModel()
) {
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/x-sqlite3")) {
      logd("uri $it")
      vm.uri = it
    }
  Text("uri ${vm.uri}")
  Button(onClick = {
    launcher.launch("backup.db")
  }) {
    Text("CreateDbBackup")
  }
}

@Composable
fun WriteDbFile(
  vm: TmpVm = viewModel()
) {
  Button(onClick = {
    vm.writeDbFile()
  }) {
    Text("writeDbFile")
  }
}