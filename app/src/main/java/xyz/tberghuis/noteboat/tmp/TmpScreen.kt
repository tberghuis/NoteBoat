package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

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
  }

}