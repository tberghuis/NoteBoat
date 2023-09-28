package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TmpScreen() {
  Scaffold(
    bottomBar = {
      BottomAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(10.dp),
      ) {
        Spacer(Modifier.weight(1f))
        FloatingActionButton(
          modifier = Modifier.padding(end = 10.dp),
          onClick = { /* doSomething() */ }) {
          Icon(Icons.Filled.MicOff, contentDescription = "Localized description")
        }
        FloatingActionButton(
          onClick = { /* doSomething() */ }) {
          Icon(Icons.Filled.Save, contentDescription = "Localized description")
        }
      }
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