package xyz.tberghuis.noteboat.tmp.tmp02

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import xyz.tberghuis.noteboat.utils.logd

@Preview
@Composable
fun FfiTest() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
  ) {
    Button(onClick = {
      logd("test")

      val hello = reverseString("hello")
      logd("hello $hello")

    }) {
      Text("test")
    }
  }
}