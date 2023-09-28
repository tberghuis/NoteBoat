package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TmpScreen() {

  Scaffold(
    bottomBar = {
      Text("bottom bar")
    },
  ) { paddingValues ->
    Column(Modifier.padding(paddingValues)) {
      TextField(
        value = "text field",
        onValueChange = {},
      )
    }
  }
}