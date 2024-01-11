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
      vm.closeDb()
    }) {
      Text("close db")
    }
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
fun CreateDocument(
  vm: TmpVm = viewModel()
) {
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/*")) {
      logd("uri $it")
      vm.uri = it
    }
  Button(onClick = {
    launcher.launch("myfilename.txt")
  }) {
    Text("CreateDocument")
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
fun WriteTxtFile(
  vm: TmpVm = viewModel()
) {
  Button(onClick = {
    vm.writeTxtFile()
  }) {
    Text("WriteTxtFile")
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
