package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TmpScreen() {

  Scaffold(
    bottomBar = {
      BottomAppBar(

      ) {
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { /* doSomething() */ }) {
          Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
        }
        IconButton(onClick = { /* doSomething() */ }) {
          Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
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