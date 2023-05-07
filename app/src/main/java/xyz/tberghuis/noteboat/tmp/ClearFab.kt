package xyz.tberghuis.noteboat.tmp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Colors
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import xyz.tberghuis.noteboat.ui.theme.NoteBoatTheme

@Composable
fun ClearFab() {

  var text by remember { mutableStateOf("") }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(onClick = {}) {
        Text("+")
      }

    },
  ) { paddingValues ->

    Column(Modifier.verticalScroll(rememberScrollState())) {



      TextField(
        value = text, onValueChange = { text = it },
        modifier = Modifier
          .fillMaxWidth()
          .padding(paddingValues)
//        .padding(bottom = 100.dp)
//          .weight(1f)
          .background(Color.Gray)
      )
      Spacer(Modifier.height(100.dp))

    }


  }


}

@Preview(showSystemUi = true)
@Composable
fun PreviewClearFab() {

  NoteBoatTheme {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
      ClearFab()
    }
  }


}