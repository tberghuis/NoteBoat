package xyz.tberghuis.noteboat.tmp

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.noteboat.utils.logd

@Composable
fun TmpScreen(
  vm: TmpVm = viewModel()
) {
  val context = LocalContext.current


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


    Button(onClick = {
      vm.createFile(context as Activity)
    }) {
      Text("createFile")
    }
    CreateDocument()

  }

}


@Composable
fun CreateDocument(
  vm: TmpVm = viewModel()
) {
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/*")) {
      logd("uri $it")
      vm.path = it?.path
    }
  Text("path ${vm.path}")
  Button(onClick = {
    launcher.launch("myfilename.txt")
  }) {
    Text("WriteTxtFile")
  }
}