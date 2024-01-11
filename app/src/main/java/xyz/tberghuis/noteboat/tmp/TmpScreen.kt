package xyz.tberghuis.noteboat.tmp

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

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


  }

}