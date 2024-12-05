package xyz.tberghuis.noteboat.tmp2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import xyz.tberghuis.noteboat.utils.logd

@Composable
fun TrashScreen() {

  LaunchedEffect(Unit) {
    logd("trash screen")
  }


  Scaffold(

  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .navigationBarsPadding(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.Start,
    )
    {
      Text(
        "hello trash screen",
        color = Color.Red,
      )
    }
  }
}